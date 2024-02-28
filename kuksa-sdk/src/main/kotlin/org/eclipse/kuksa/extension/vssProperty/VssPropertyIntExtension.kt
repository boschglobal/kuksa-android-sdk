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
 */

package org.eclipse.kuksa.extension.vssProperty

import org.eclipse.kuksa.extension.copy
import org.eclipse.kuksa.vsscore.model.VssLeaf

/**
 * Convenience operator for [copy] which updates the [VssLeaf.value] by adding [value] to it.
 *
 * @throws [IllegalArgumentException] if the copied types do not match.
 * @throws [NoSuchElementException] if no copy method was found for the class.
 */
operator fun VssLeaf<Int>.plusAssign(value: Number) {
    copy(this.value + value.toInt())
}

/**
 * Convenience operator for [copy] which updates the [VssLeaf.value] by adding [value] to it.
 *
 * @throws [IllegalArgumentException] if the copied types do not match.
 * @throws [NoSuchElementException] if no copy method was found for the class.
 */
operator fun VssLeaf<Int>.plus(value: Number): VssLeaf<Int> {
    return copy(this.value + value.toInt())
}

/**
 * Convenience operator for [copy] which updates the [VssLeaf.value] by subtracting [value] to it.
 *
 * @throws [IllegalArgumentException] if the copied types do not match.
 * @throws [NoSuchElementException] if no copy method was found for the class.
 */
operator fun VssLeaf<Int>.minusAssign(value: Number) {
    copy(this.value - value.toInt())
}

/**
 * Convenience operator for [copy] which updates the [VssLeaf.value] by subtracting [value] to it.
 *
 * @throws [IllegalArgumentException] if the copied types do not match.
 * @throws [NoSuchElementException] if no copy method was found for the class.
 */
operator fun VssLeaf<Int>.minus(value: Number): VssLeaf<Int> {
    return copy(this.value - value.toInt())
}

/**
 * Convenience operator for [copy] which updates the [VssLeaf.value] by dividing [value] to it.
 *
 * @throws [IllegalArgumentException] if the copied types do not match.
 * @throws [NoSuchElementException] if no copy method was found for the class.
 * @throws [ArithmeticException] if divided by zero.
 */
operator fun VssLeaf<Int>.divAssign(value: Number) {
    copy(this.value / value.toInt())
}

/**
 * Convenience operator for [copy] which updates the [VssLeaf.value] by dividing [value] to it.
 *
 * @throws [IllegalArgumentException] if the copied types do not match.
 * @throws [NoSuchElementException] if no copy method was found for the class.
 * @throws [ArithmeticException] if divided by zero.
 */
operator fun VssLeaf<Int>.div(value: Number): VssLeaf<Int> {
    return copy(this.value / value.toInt())
}

/**
 * Convenience operator for [copy] which updates the [VssLeaf.value] by multiplying [value] to it.
 *
 * @throws [IllegalArgumentException] if the copied types do not match.
 * @throws [NoSuchElementException] if no copy method was found for the class.
 */
operator fun VssLeaf<Int>.timesAssign(value: Number) {
    copy(this.value * value.toInt())
}

/**
 * Convenience operator for [copy] which updates the [VssLeaf.value] by multiplying [value] to it.
 *
 * @throws [IllegalArgumentException] if the copied types do not match.
 * @throws [NoSuchElementException] if no copy method was found for the class.
 */
operator fun VssLeaf<Int>.times(value: Number): VssLeaf<Int> {
    return copy(this.value * value.toInt())
}
