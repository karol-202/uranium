package pl.karol202.uranium.swing.control.filler

import java.awt.Component
import javax.swing.Box

enum class StrutAxis(private val constructor: (width: Int) -> Component)
{
	HORIZONTAL({ Box.createHorizontalStrut(it) }),
	VERTICAL({ Box.createVerticalStrut(it) });

	fun createStrut(width: Int) = constructor(width) as Box.Filler
}
