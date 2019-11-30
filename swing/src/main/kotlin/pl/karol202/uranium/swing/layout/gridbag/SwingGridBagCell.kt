package pl.karol202.uranium.swing.layout.gridbag

import pl.karol202.uranium.swing.layout.flow.flowLayout
import pl.karol202.uranium.swing.native.SNCProvider
import pl.karol202.uranium.swing.native.constraints
import pl.karol202.uranium.swing.util.*

// Insets are the first argument in order to force to use named arguments
fun <EP : SNCProvider<EP>> SwingGridBagScope.cell(insets: Insets = insets(),
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
                                                  element: SwingRenderScope.() -> SwingElement<EP>) =
		SwingEmptyRenderScope.element().constraints(GridBagConstraints(x, y, width, height, weightX, weightY, anchor,
		                                                               fillX, fillY, insets, paddingX, paddingY))

fun SwingGridBagScope.panelCell(insets: Insets = insets(),
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
		SwingEmptyRenderScope.element().let { elem ->
			SwingEmptyRenderScope.flowLayout(key = elem.key) { + elem }
					.constraints(GridBagConstraints(x, y, width, height, weightX, weightY, anchor, fillX, fillY, insets,
					                                paddingX, paddingY))
		}
