@file:JvmName("NativeListCommonKt") // Workaround for bug "JVM class name duplication"
package pl.karol202.uranium.core.util

import kotlin.jvm.JvmName

/**
 * Collection wrapper, created because ArrayList in Kotlin/JS was too slow due to type checking.
 * - on JVM, it uses ArrayList
 * - in JS, it uses array
 * - in Kotlin/Native it used ArrayList
 */
interface NativeList<out T> : Iterable<T>
{
	val size: Int

	override operator fun iterator(): Iterator<T>

	operator fun get(index: Int): T

	operator fun plus(element: @UnsafeVariance T): NativeList<T>

	operator fun plus(elements: NativeList<@UnsafeVariance T>): NativeList<T>

	operator fun minus(element: @UnsafeVariance T): NativeList<T>

	operator fun minus(elements: NativeList<@UnsafeVariance T>): NativeList<T>

	fun <R> map(transform: (T) -> R): NativeList<R>

	fun <R> mapIndexed(transform: (Int, T) -> R): NativeList<R>

	fun <R : Any> mapIndexedNotNull(transform: (Int, T) -> R?): NativeList<R>

	fun <R> flatMap(transform: (T) -> NativeList<R>): NativeList<R>

	fun filter(predicate: (T) -> Boolean): NativeList<T>

	fun filterNot(predicate: (T) -> Boolean): NativeList<T>

	fun take(n: Int): NativeList<T>

	fun drop(n: Int): NativeList<T>

	fun inserted(element: @UnsafeVariance T, index: Int): NativeList<T>

	fun replaced(newElement: @UnsafeVariance T, index: Int): NativeList<T>
}

expect fun <T> emptyNativeList(): NativeList<T>

expect fun <T> nativeListOf(vararg elements: T): NativeList<T>

expect fun <T> Iterable<T>.toNativeList(): NativeList<T>

fun <T> NativeList<T>.removed(element: T) = filterNot { it == element }
