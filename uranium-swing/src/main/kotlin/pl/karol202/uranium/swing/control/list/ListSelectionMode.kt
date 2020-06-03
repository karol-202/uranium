package pl.karol202.uranium.swing.control.list

import javax.swing.ListSelectionModel

enum class ListSelectionMode(val code: Int)
{
	SINGLE_SELECTION(ListSelectionModel.SINGLE_SELECTION),
	SINGLE_INTERVAL_SELECTION(ListSelectionModel.SINGLE_INTERVAL_SELECTION),
	MULTIPLE_INTERNAL_SELECTION(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
}
