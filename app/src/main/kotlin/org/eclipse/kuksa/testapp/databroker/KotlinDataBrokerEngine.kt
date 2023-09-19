/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 */

package org.eclipse.kuksa.testapp.databroker

import android.content.res.AssetManager
import androidx.lifecycle.LifecycleCoroutineScope
import io.grpc.ChannelCredentials
import io.grpc.Grpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.TlsChannelCredentials
import kotlinx.coroutines.launch
import org.eclipse.kuksa.CoroutineCallback
import org.eclipse.kuksa.DataBrokerConnection
import org.eclipse.kuksa.DataBrokerConnector
import org.eclipse.kuksa.DataBrokerException
import org.eclipse.kuksa.PropertyObserver
import org.eclipse.kuksa.TimeoutConfig
import org.eclipse.kuksa.VssPropertyObserver
import org.eclipse.kuksa.model.Property
import org.eclipse.kuksa.proto.v1.KuksaValV1.GetResponse
import org.eclipse.kuksa.proto.v1.KuksaValV1.SetResponse
import org.eclipse.kuksa.proto.v1.Types
import org.eclipse.kuksa.proto.v1.Types.Datapoint
import org.eclipse.kuksa.testapp.extension.open
import org.eclipse.kuksa.testapp.model.ConnectionInfo
import org.eclipse.kuksa.vsscore.model.VssSpecification
import java.io.IOException

class KotlinDataBrokerEngine(
    private val lifecycleScope: LifecycleCoroutineScope,
    private val assetManager: AssetManager,
) : DataBrokerEngine {
    override var dataBrokerConnection: DataBrokerConnection? = null

    override fun connect(
        connectionInfo: ConnectionInfo,
        callback: CoroutineCallback<DataBrokerConnection>,
    ) {
        if (connectionInfo.isTlsEnabled) {
            connectSecure(connectionInfo, callback)
        } else {
            connectInsecure(connectionInfo, callback)
        }
    }

    private fun connectInsecure(
        connectInfo: ConnectionInfo,
        callback: CoroutineCallback<DataBrokerConnection>,
    ) {
        try {
            val managedChannel = ManagedChannelBuilder
                .forAddress(connectInfo.host, connectInfo.port)
                .usePlaintext()
                .build()

            connect(managedChannel, callback)
        } catch (e: IllegalArgumentException) {
            callback.onError(e)
        }
    }

    private fun connectSecure(
        connectInfo: ConnectionInfo,
        callback: CoroutineCallback<DataBrokerConnection>,
    ) {
        val rootCert = connectInfo.certificate

        val tlsCredentials: ChannelCredentials
        try {
            val rootCertFile = assetManager.open(rootCert)
            tlsCredentials = TlsChannelCredentials.newBuilder()
                .trustManager(rootCertFile)
                .build()
        } catch (e: IOException) {
            callback.onError(e)
            return
        }

        try {
            val host = connectInfo.host.trim()
            val port = connectInfo.port
            val channelBuilder = Grpc
                .newChannelBuilderForAddress(host, port, tlsCredentials)

            val overrideAuthority = rootCert.overrideAuthority.trim()
            val hasOverrideAuthority = overrideAuthority.isNotEmpty()
            if (hasOverrideAuthority) {
                channelBuilder.overrideAuthority(overrideAuthority)
            }

            val managedChannel = channelBuilder.build()
            connect(managedChannel, callback)
        } catch (e: IllegalArgumentException) {
            callback.onError(e)
        }
    }

    private fun connect(managedChannel: ManagedChannel, callback: CoroutineCallback<DataBrokerConnection>) {
        val connector = DataBrokerConnector(managedChannel).apply {
            timeoutConfig = TimeoutConfig(TIMEOUT_CONNECTION_SEC)
        }

        lifecycleScope.launch {
            try {
                dataBrokerConnection = connector.connect()
                callback.onSuccess(dataBrokerConnection)
            } catch (e: DataBrokerException) {
                callback.onError(e)
            }
        }
    }

    override fun fetchProperty(property: Property, callback: CoroutineCallback<GetResponse>) {
        lifecycleScope.launch {
            try {
                val response = dataBrokerConnection?.fetchProperty(property) ?: return@launch
                callback.onSuccess(response)
            } catch (e: DataBrokerException) {
                callback.onError(e)
            }
        }
    }

    override fun updateProperty(
        property: Property,
        datapoint: Datapoint,
        callback: CoroutineCallback<SetResponse>,
    ) {
        lifecycleScope.launch {
            try {
                val response = dataBrokerConnection?.updateProperty(property, datapoint) ?: return@launch
                callback.onSuccess(response)
            } catch (e: DataBrokerException) {
                callback.onError(e)
            }
        }
    }

    override fun subscribe(property: Property, propertyObserver: PropertyObserver) {
        val properties = listOf(property)
        dataBrokerConnection?.subscribe(properties, propertyObserver)
    }

    override fun <T : VssSpecification> subscribe(
        specification: T,
        fields: List<Types.Field>,
        propertyObserver: VssPropertyObserver<T>,
    ) {
        dataBrokerConnection?.subscribe(specification, fields, propertyObserver)
    }

    override fun disconnect() {
        dataBrokerConnection?.disconnect()
    }

    companion object {
        const val TIMEOUT_CONNECTION_SEC = 5L
    }
}
