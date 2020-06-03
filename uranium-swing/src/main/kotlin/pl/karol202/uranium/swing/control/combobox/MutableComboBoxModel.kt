package pl.karol202.uranium.swing.control.combobox

import javax.swing.ComboBoxModel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class MutableComboBoxModel<E>(items: List<E>) : ComboBoxModel<E>
{
	private val contentEvent = ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0)

	private var listeners = emptyList<ListDataListener>()

	var items: List<E> = items
		set(value)
		{
			field = value
			fireContentChange()
		}
	private var selectedIndex: Int? = null

	override fun getSize() = items.size

	override fun getElementAt(index: Int) = items[index]

	override fun getSelectedItem() = selectedIndex?.let { items[it] }

	override fun setSelectedItem(item: Any?)
	{
		selectedIndex = items.indexOf(item).takeIf { it != -1 }
		fireContentChange()
	}

	private fun fireContentChange() = listeners.forEach { it.contentsChanged(contentEvent) }

	override fun addListDataListener(listener: ListDataListener)
	{
		listeners += listener
	}

	override fun removeListDataListener(listener: ListDataListener)
	{
		listeners -= listener
	}
}
