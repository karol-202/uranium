package pl.karol202.uranium.core.util

private inline class NativeListImpl<out T>(private val list: MutableList<T>) : NativeList<T>
{
	override val size get() = list.size

	override operator fun iterator() = list.iterator() as Iterator<T>

	override operator fun get(index: Int) = list[index]

	override operator fun plus(element: @UnsafeVariance T) = (list + element).toNativeList()

	override operator fun plus(elements: NativeList<@UnsafeVariance T>) = (list + elements).toNativeList()

	override operator fun minus(element: @UnsafeVariance T) = (list - element).toNativeList()

	override operator fun minus(elements: NativeList<@UnsafeVariance T>) = (list - elements).toNativeList()

	override fun <R> map(transform: (T) -> R) = list.map(transform).toNativeList()

	override fun <R> mapIndexed(transform: (Int, T) -> R) = list.mapIndexed(transform).toNativeList()

	override fun <R : Any> mapIndexedNotNull(transform: (Int, T) -> R?) = list.mapIndexedNotNull(transform).toNativeList()

	override fun <R> flatMap(transform: (T) -> NativeList<R>) = list.flatMap(transform).toNativeList()

	override fun filter(predicate: (T) -> Boolean) = list.filter(predicate).toNativeList()

	override fun filterNot(predicate: (T) -> Boolean) = list.filterNot(predicate).toNativeList()

	override fun take(n: Int) = list.take(n).toNativeList()

	override fun drop(n: Int) = list.drop(n).toNativeList()

	override fun inserted(element: @UnsafeVariance T, index: Int): NativeList<T>
	{
		val newList = list.toMutableList()
		newList.add(index, element)
		return NativeListImpl(newList)
	}

	override fun replaced(newElement: @UnsafeVariance T, index: Int): NativeList<T>
	{
		val newList = list.toMutableList()
		newList[index] = newElement
		return NativeListImpl(newList)
	}
}

actual fun <T> emptyNativeList(): NativeList<T> = NativeListImpl(mutableListOf())

actual fun <T> nativeListOf(vararg elements: T): NativeList<T> = NativeListImpl(elements.toMutableList())

actual fun <T> Iterable<T>.toNativeList(): NativeList<T> = NativeListImpl(toMutableList())
