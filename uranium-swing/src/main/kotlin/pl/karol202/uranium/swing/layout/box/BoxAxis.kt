package pl.karol202.uranium.swing.layout.box

import javax.swing.BoxLayout

enum class BoxAxis(val code: Int)
{
	X(BoxLayout.X_AXIS),
	Y(BoxLayout.Y_AXIS),
	LINE(BoxLayout.LINE_AXIS),
	PAGE(BoxLayout.PAGE_AXIS)
}
