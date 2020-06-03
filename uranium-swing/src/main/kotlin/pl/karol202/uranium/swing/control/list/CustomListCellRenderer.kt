package pl.karol202.uranium.swing.control.list

import pl.karol202.uranium.core.manager.RenderManager
import pl.karol202.uranium.swing.native.SwingNative
import pl.karol202.uranium.swing.util.*
import java.awt.BorderLayout
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

class CustomListCellRenderer<E>(var renderFunction: SwingRenderScope.(Props<E>) -> SwingElement<*>) : ListCellRenderer<E>
{
	data class Props<E>(val item: E?,
	                    val index: Int,
	                    val selected: Boolean,
	                    val hasFocus: Boolean)

	private val containerComponent = JPanel(BorderLayout())
	private val container = SwingNative.container(containerComponent)
	private var renderManager: SwingRenderManager<SwingSingleWrapper.Props>? = null

	override fun getListCellRendererComponent(list: JList<out E>?, value: E?, index: Int, selected: Boolean, focus: Boolean) =
			containerComponent.also { reuseOrRender(Props(value, index, selected, focus)) }

	private fun reuseOrRender(props: Props<E>) = reuse(props) ?: render(props)

	private fun reuse(props: Props<E>) = renderManager?.reuse(renderRoot(props).props)

	private fun render(props: Props<E>) = RenderManager(renderRoot(props), container).also { renderManager = it }.init()

	private fun renderRoot(props: Props<E>) = renderScope().singleWrapper { renderFunction(props) }
}
