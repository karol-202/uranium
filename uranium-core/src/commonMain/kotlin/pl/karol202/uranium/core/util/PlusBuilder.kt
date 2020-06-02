package pl.karol202.uranium.core.util

interface PlusBuilder<T>
{
	operator fun T.unaryPlus()

	operator fun List<T>.unaryPlus()
}

abstract class PlusBuilderImpl<T> : PlusBuilder<T>
{
	private val _elements = arrayListOf<T>()
	val elements: List<T> = _elements

	override operator fun T.unaryPlus()
	{
		_elements.add(this)
	}

	override fun List<T>.unaryPlus()
	{
		_elements.addAll(this)
	}
}
