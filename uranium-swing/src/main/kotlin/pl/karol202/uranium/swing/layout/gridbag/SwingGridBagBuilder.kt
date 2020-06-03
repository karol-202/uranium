package pl.karol202.uranium.swing.layout.gridbag

import pl.karol202.uranium.core.util.PlusBuilder
import pl.karol202.uranium.core.util.PlusBuilderImpl
import pl.karol202.uranium.swing.util.SwingElement

interface SwingGridBagBuilder : PlusBuilder<GridBagCell>, SwingGridBagScope

private class SwingGridBagBuilderImpl : PlusBuilderImpl<GridBagCell>(), SwingGridBagBuilder

fun (SwingGridBagBuilder.() -> Unit).render() = SwingGridBagBuilderImpl().also(this).elements
