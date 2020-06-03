package pl.karol202.uranium.swing.util

import pl.karol202.uranium.core.manager.RenderManager
import pl.karol202.uranium.swing.native.SwingNative
import java.awt.BorderLayout
import javax.swing.JPanel

class EmbeddedRenderer
{
	val component = JPanel(BorderLayout())
	private val container = SwingNative.container(component)
	private var renderManager: SwingRenderManager<SwingSingleWrapper.Props>? = null

	fun update(rootElement: SwingElement<*>) = reuseOrRender(rootElement)

	private fun reuseOrRender(rootElement: SwingElement<*>) = reuse(rootElement) ?: render(rootElement)

	private fun reuse(rootElement: SwingElement<*>) = renderManager?.reuse(rootElement.wrapped.props)

	private fun render(rootElement: SwingElement<*>) = RenderManager(rootElement.wrapped, container).also { renderManager = it }.init()

	private val SwingElement<*>.wrapped get() = let { element -> renderScope().singleWrapper { element } }
}
