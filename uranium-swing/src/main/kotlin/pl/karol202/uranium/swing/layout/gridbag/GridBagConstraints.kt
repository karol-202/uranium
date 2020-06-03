package pl.karol202.uranium.swing.layout.gridbag

import pl.karol202.uranium.swing.util.Insets
import java.awt.GridBagConstraints

// Decision about making this class a subclass of Swing's GridBagConstraints has been made because of need for
// being constraints comparable by contents and not by instance (like Swing's GridBagConstraints).
data class GridBagConstraints(private val x: Int,
                              private val y: Int,
                              private val width: Int,
                              private val height: Int,
                              private val weightX: Double,
                              private val weightY: Double,
                              private val anchor: GridBagAnchor,
                              private val fillX: Boolean,
                              private val fillY: Boolean,
                              private val insets: Insets,
                              private val paddingX: Int,
                              private val paddingY: Int) : GridBagConstraints(x, y, width, height, weightX, weightY,
                                                                              anchor.code, GridBagFill.fill(fillX, fillY).code,
                                                                              insets.toAWTInsets(), paddingX, paddingY)
