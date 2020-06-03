package pl.karol202.uranium.swing.control.scrollpane

import pl.karol202.uranium.swing.control.scrollbar.ScrollBarAxis
import pl.karol202.uranium.swing.control.scrollbar.ScrollBarAxis.HORIZONTAL
import pl.karol202.uranium.swing.control.scrollbar.ScrollBarAxis.VERTICAL
import javax.swing.ScrollPaneConstants

enum class ScrollBarPolicy(val codeProvider: (ScrollBarAxis) -> Int)
{
	ALWAYS({ when(it)
	{
		HORIZONTAL -> ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
		VERTICAL -> ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
	} }),
	NEVER({ when(it)
	{
		HORIZONTAL -> ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
		VERTICAL -> ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
	} }),
	AS_NEEDED({ when(it)
	{
		HORIZONTAL -> ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
		VERTICAL -> ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
	} })
}
