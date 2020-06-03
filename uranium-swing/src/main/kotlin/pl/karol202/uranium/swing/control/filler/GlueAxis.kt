package pl.karol202.uranium.swing.control.filler

import java.awt.Component
import javax.swing.Box

enum class GlueAxis(private val constructor: () -> Component)
{
	HORIZONTAL({ Box.createHorizontalGlue() }),
	VERTICAL({ Box.createVerticalGlue() }),
	BOTH({ Box.createGlue() });

	fun createGlue() = constructor() as Box.Filler
}
