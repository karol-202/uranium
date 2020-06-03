package pl.karol202.uranium.swing.control.list

import javax.swing.AbstractListModel

class MutableListModel<E>(items: List<E>) : AbstractListModel<E>()
{
	var items = items
		set(value)
		{
			field = value
			fireContentsChanged(this, 0, 0)
		}

	override fun getElementAt(index: Int) = items[index]

	override fun getSize() = items.size
}
