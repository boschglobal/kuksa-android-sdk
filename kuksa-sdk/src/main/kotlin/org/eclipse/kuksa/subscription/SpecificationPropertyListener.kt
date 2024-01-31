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

package org.eclipse.kuksa.subscription

import org.eclipse.kuksa.PropertyListener
import org.eclipse.kuksa.VssSpecificationListener
import org.eclipse.kuksa.extension.copy
import org.eclipse.kuksa.proto.v1.KuksaValV1
import org.eclipse.kuksa.vsscore.model.VssSpecification

internal class SpecificationPropertyListener<T : VssSpecification>(
    specification: T,
    private val listener: VssSpecificationListener<T>,
) : PropertyListener {
    // This is currently needed because we get multiple subscribe responses for every heir. Otherwise we
    // would override the last heir value with every new response.
    private var updatedVssSpecification: T = specification

    override fun onPropertyChanged(entryUpdates: List<KuksaValV1.EntryUpdate>) {
        entryUpdates.forEach { entryUpdate ->
            val dataEntry = entryUpdate.entry
            updatedVssSpecification = updatedVssSpecification.copy(dataEntry.path, dataEntry.value)
        }

        listener.onSpecificationChanged(updatedVssSpecification)
    }

    override fun onError(throwable: Throwable) {
        listener.onError(throwable)
    }

    // Two SpecificationObserverWrapper instances are equal if they have the same observer set!
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpecificationPropertyListener<*>

        return listener == other.listener
    }

    override fun hashCode(): Int {
        return listener.hashCode()
    }
}
