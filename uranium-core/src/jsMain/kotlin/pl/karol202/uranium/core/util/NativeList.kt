package pl.karol202.uranium.core.util

private inline class NativeListImpl<out T>(private val array: dynamic) : NativeList<T>
{
	override val size get() = array.length

	override operator fun get(index: Int) = array[index]

	override operator fun iterator() = array.iterator()

	override operator fun plus(element: @UnsafeVariance T) = NativeListImpl<T>(js("[]").concat(array, element))

	override operator fun plus(elements: NativeList<@UnsafeVariance T>) =
			NativeListImpl<T>(js("[]").concat(array, elements.asImpl().array))

	override operator fun minus(element: @UnsafeVariance T) = (this.asIterable() - element).toNativeList()

	override operator fun minus(elements: NativeList<@UnsafeVariance T>) = (this.asIterable() - elements).toNativeList()

	override fun <R> map(transform: (T) -> R): NativeList<R>
	{
		val dest = js("[]")
		for(element in asIterable())
			dest.push(transform(element))

		return NativeListImpl(dest)
	}

	override fun <R> mapIndexed(transform: (Int, T) -> R): NativeList<R>
	{
		var index = 0
		val dest = js("[]")
		for(element in asIterable())
			dest.push(transform(index++, element))
		return NativeListImpl(dest)
	}

	override fun <R : Any> mapIndexedNotNull(transform: (Int, T) -> R?): NativeList<R>
	{
		var index = 0
		val dest = js("[]")
		for(element in asIterable())
		{
			val transformed = transform(index++, element)
			if(transformed != null) dest.push(transformed)
		}
		return NativeListImpl(dest)
	}

	override fun <R> flatMap(transform: (T) -> NativeList<R>): NativeList<R>
	{
		val dest = js("[]")
		for(element in asIterable())
			for(newElement in transform(element))
				dest.push(newElement)
		return NativeListImpl(dest)
	}

	override fun filter(predicate: (T) -> Boolean): NativeList<T>
	{
		val dest = js("[]")
		for(element in asIterable())
			if(predicate(element)) dest.push(element)
		return NativeListImpl(dest)
	}

	override fun filterNot(predicate: (T) -> Boolean) = filter { !predicate(it) }

	override fun take(n: Int): NativeList<T> = NativeListImpl(array.slice(0, n))

	override fun drop(n: Int): NativeList<T> = NativeListImpl(array.slice(n))

	override fun inserted(element: @UnsafeVariance T, index: Int): NativeList<T>
	{
		val newArray = array.slice(0)
		newArray.splice(index, 0, element)
		return NativeListImpl(newArray)
	}

	override fun replaced(newElement: @UnsafeVariance T, index: Int): NativeList<T>
	{
		val newArray = array.slice(0)
		newArray.splice(index, 1, newElement)
		return NativeListImpl(newArray)
	}
}

actual fun <T> emptyNativeList(): NativeList<T> = NativeListImpl(js("[]"))

actual fun <T> nativeListOf(vararg elements: T): NativeList<T> = NativeListImpl(elements)

actual fun <T> Iterable<T>.toNativeList(): NativeList<T>
{
	val array = js("[]")
	for(element in this) array.push(element)
	return NativeListImpl(array)
}

private fun <T> NativeList<T>.asImpl() = this as NativeListImpl<T>
