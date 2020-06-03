package pl.karol202.uranium.swing.layout.gridbag

import pl.karol202.uranium.core.render.render
import pl.karol202.uranium.swing.util.Insets
import pl.karol202.uranium.swing.util.SwingElement
import pl.karol202.uranium.swing.util.SwingRenderScope
import pl.karol202.uranium.swing.util.renderScope

data class GridBagCell(val content: SwingElement<*>,
                       val constraints: GridBagConstraints)

// Insets are the first argument in order to force to use named arguments
fun SwingGridBagScope.cell(insets: Insets = insets(),
                           x: Int,
                           y: Int,
                           width: Int = 1,
                           height: Int = 1,
                           weightX: Double = 0.0,
                           weightY: Double = 0.0,
                           anchor: GridBagAnchor = GridBagAnchor.CENTER,
                           fillX: Boolean = false,
                           fillY: Boolean = false,
                           paddingX: Int = 0,
                           paddingY: Int = 0,
                           element: SwingRenderScope.() -> SwingElement<*>) =
		GridBagCell(element.render(), GridBagConstraints(x, y, width, height, weightX, weightY, anchor,
		                                                 fillX, fillY, insets, paddingX, paddingY))

fun SwingGridBagScope.insets(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) = Insets(left, top, right, bottom)
