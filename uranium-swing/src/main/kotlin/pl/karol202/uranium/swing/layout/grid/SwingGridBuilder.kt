package pl.karol202.uranium.swing.layout.grid

import pl.karol202.uranium.core.render.RenderDsl
import pl.karol202.uranium.core.render.render
import pl.karol202.uranium.swing.util.SwingElement
import pl.karol202.uranium.swing.util.SwingRenderBuilder

// RenderDsl annotation prohibits use of nested 'column' or 'row' method
@RenderDsl
abstract class SwingGridBuilder
{
	protected var sequences = emptyList<List<SwingElement<*>>>()
		private set

	protected val sequencesAmount get() = sequences.size
	protected val sequenceSize get() = sequences.map { it.size }.distinct().singleOrNull()
			?: throw IllegalStateException("Inconsistent rows/columns amount")

	abstract val rowsAmount: Int
	abstract val columnsAmount: Int
	abstract val elements: List<SwingElement<*>>

	protected fun sequence(block: SwingRenderBuilder.() -> Unit)
	{
		sequences = sequences.plus(element = block.render())
	}
}

@RenderDsl
class SwingGridRowsBuilder : SwingGridBuilder()
{
	override val rowsAmount get() = sequencesAmount
	override val columnsAmount get() = sequenceSize
	override val elements get() = sequences.flatten()

	fun row(block: SwingRenderBuilder.() -> Unit) = sequence(block)
}

@RenderDsl
class SwingGridColumnsBuilder : SwingGridBuilder()
{
	override val rowsAmount get() = sequenceSize
	override val columnsAmount get() = sequencesAmount
	override val elements get() = (0 until rowsAmount).flatMap { rowIndex -> sequences.map { it[rowIndex] } }

	fun column(block: SwingRenderBuilder.() -> Unit) = sequence(block)
}
