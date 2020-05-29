package pl.karol202.uranium.webcanvas.component.primitives

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.webcanvas.WCRenderScope
import pl.karol202.uranium.webcanvas.component.draw.drawComponent
import pl.karol202.uranium.webcanvas.values.clearViewport

fun WCRenderScope.viewportClear(key: Any = AutoKey) = drawComponent(key = key) { clearViewport() }
