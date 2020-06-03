package pl.karol202.uranium.swing.util

import javax.swing.SwingConstants

enum class HorizontalAlign(val code: Int)
{
	LEFT(SwingConstants.LEFT),
	CENTER(SwingConstants.CENTER),
	RIGHT(SwingConstants.RIGHT),
	LEADING(SwingConstants.LEADING),
	TRAILING(SwingConstants.TRAILING)
}

enum class VerticalAlign(val code: Int)
{
	TOP(SwingConstants.TOP),
	CENTER(SwingConstants.CENTER),
	BOTTOM(SwingConstants.BOTTOM)
}
