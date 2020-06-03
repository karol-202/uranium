package pl.karol202.uranium.swing.control.combobox

import pl.karol202.uranium.core.manager.RenderManager
import pl.karol202.uranium.swing.native.SwingNative
import pl.karol202.uranium.swing.util.*
import java.awt.BorderLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.ComboBoxEditor
import javax.swing.JPanel

class CustomComboBoxEditor<E>(var renderFunction: SwingRenderScope.(Props<E>) -> SwingElement<*>) : ComboBoxEditor
{
	companion object
	{
		private val ACTION_EVENT = ActionEvent(this, ActionEvent.ACTION_PERFORMED, null)
	}

	data class Props<E>(val item: E?,
	                    val onEdit: (E) -> Unit)

	private val listeners = mutableListOf<ActionListener>()
	private val containerComponent = JPanel(BorderLayout())
	private val container = SwingNative.container(containerComponent)
	private var renderManager: SwingRenderManager<SwingSingleWrapper.Props>? = null

	private var item: E? = null

	override fun getEditorComponent() = containerComponent

	override fun selectAll() { }

	override fun getItem() = item

	@Suppress("UNCHECKED_CAST")
	override fun setItem(rawItem: Any?)
	{
		this.item = rawItem as? E
		reuseOrRender(Props(item) { onEdit(it) })
	}

	private fun reuseOrRender(props: Props<E>) = reuse(props) ?: render(props)

	private fun reuse(props: Props<E>) = renderManager?.reuse(renderRoot(props).props)

	private fun render(props: Props<E>) = RenderManager(renderRoot(props), container).also { renderManager = it }.init()

	private fun renderRoot(props: Props<E>) = renderScope().singleWrapper { renderFunction(props) }

	private fun onEdit(newItem: E)
	{
		item = newItem
		fireEvent()
	}

	private fun fireEvent() = listeners.forEach { it.actionPerformed(ACTION_EVENT) }

	override fun addActionListener(listener: ActionListener)
	{
		listeners += listener
	}

	override fun removeActionListener(listener: ActionListener)
	{
		listeners -= listener
	}
}
