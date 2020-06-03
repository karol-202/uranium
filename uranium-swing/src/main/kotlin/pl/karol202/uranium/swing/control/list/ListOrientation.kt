package pl.karol202.uranium.swing.control.list

import javax.swing.JList

enum class ListOrientation(val code: Int)
{
	VERTICAL(JList.VERTICAL),
	HORIZONTAL_WRAP(JList.HORIZONTAL_WRAP),
	VERTICAL_WRAP(JList.VERTICAL_WRAP)
}
