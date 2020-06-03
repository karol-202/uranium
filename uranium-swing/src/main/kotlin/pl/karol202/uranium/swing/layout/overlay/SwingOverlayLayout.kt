package pl.karol202.uranium.swing.layout.overlay

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.core.render.render
import pl.karol202.uranium.swing.layout.layout
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.Container
import java.awt.LayoutManager
import javax.swing.OverlayLayout

class SwingOverlayLayout(props: Props) : SwingAbstractAppComponent<SwingOverlayLayout.Props>(props)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val content: List<SwingElement<*>> = emptyList()) : UProps,
	                                                                     SwingNativeComponent.PropsProvider<Props>,
	                                                                     PropsProvider<Props>
	{
		override val overlayLayoutProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())

		override fun withOverlayLayoutProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val overlayLayoutProps: Props

		fun withOverlayLayoutProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() = layout(swingProps = props.swingProps,
	                                                   content = props.content,
	                                                   layoutUpdater = ::updateLayout)

	private fun updateLayout(container: Container, layout: LayoutManager?) =
			layout as? OverlayLayout ?: OverlayLayout(container)
}

fun SwingRenderScope.overlayLayout(key: Any = AutoKey,
                                   block: SwingRenderBuilder.() -> Unit) =
		overlayLayout(SwingOverlayLayout.Props(key)).content(block)

internal fun SwingRenderScope.overlayLayout(props: SwingOverlayLayout.Props) = component(::SwingOverlayLayout, props)

private typealias SOLProvider<P> = SwingOverlayLayout.PropsProvider<P>
fun <P : SOLProvider<P>> SwingElement<P>.withOverlayLayoutProps(builder: Builder<SwingOverlayLayout.Props>) =
		withProps { withOverlayLayoutProps(builder) }
fun <P : SOLProvider<P>> SwingElement<P>.content(block: SwingRenderBuilder.() -> Unit) =
		withOverlayLayoutProps { copy(content = block.render()) }
