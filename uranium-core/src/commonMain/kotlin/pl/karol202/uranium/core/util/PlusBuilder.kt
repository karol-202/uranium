package pl.karol202.uranium.core.util

/**
 * [PlusBuilder] allows to create a DSL with possibility
 * of adding items or list of items to a builder using unary plus.
 *
 * @param T element type
 */
interface PlusBuilder<T>
{
	operator fun T.unaryPlus()

	operator fun List<T>.unaryPlus()
}

internal abstract class PlusBuilderImpl<T> : PlusBuilder<T>
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
