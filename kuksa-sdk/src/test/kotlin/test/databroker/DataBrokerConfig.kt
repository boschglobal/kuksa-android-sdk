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

package test.databroker

import java.util.concurrent.TimeUnit

object DataBrokerConfig {
    const val HOST = "127.0.0.1"
    const val PORT = 55556

    // low timeout should be okay, since we are testing against a local service
    const val TIMEOUT_SECONDS = 3L
    val TIMEOUT_UNIT = TimeUnit.SECONDS
}
