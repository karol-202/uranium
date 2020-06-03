package pl.karol202.uranium.swing.control.scrollpane

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.core.render.render
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import pl.karol202.uranium.swing.util.update
import java.awt.Component
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants.*

class SwingScrollPaneUnidirectional(private val nativeComponent: JScrollPane,
                                    initialProps: Props) : SwingAbstractAppComponent<SwingScrollPaneUnidirectional.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val scrollPaneBaseProps: SwingScrollPaneBase.Props = SwingScrollPaneBase.Props(),
	                 val upperLeftCorner: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val upperRightCorner: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val lowerLeftCorner: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val lowerRightCorner: Prop<SwingElement<*>?> = Prop.NoValue) : UProps,
	                                                                                SwingNativeComponent.PropsProvider<Props>,
	                                                                                SwingScrollPaneBase.PropsProvider<Props>,
	                                                                                PropsProvider<Props>
	{
		override val swingProps = scrollPaneBaseProps.swingProps
		override val scrollPaneUnidirectionalProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(scrollPaneBaseProps = scrollPaneBaseProps.withSwingProps(builder))

		override fun withScrollPaneBaseProps(builder: Builder<SwingScrollPaneBase.Props>) =
				copy(scrollPaneBaseProps = scrollPaneBaseProps.builder())

		override fun withScrollPaneUnidirectionalProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val scrollPaneUnidirectionalProps: Props

		fun withScrollPaneUnidirectionalProps(builder: Builder<Props>): S
	}

	private var upperLeftCornerRenderer = EmbeddedRenderer()
	private var upperRightCornerRenderer = EmbeddedRenderer()
	private var lowerLeftCornerRenderer = EmbeddedRenderer()
	private var lowerRightCornerRenderer = EmbeddedRenderer()

	override fun URenderScope<Swing>.render() =
			scrollPaneBase(nativeComponent = { nativeComponent }, props = props.scrollPaneBaseProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
		props.upperLeftCorner.ifPresent { corner ->
			updateRenderer(upperLeftCornerRenderer, corner, getCorner(UPPER_LEFT_CORNER)) { setCorner(UPPER_LEFT_CORNER, it) }
		}
		props.upperRightCorner.ifPresent { corner ->
			updateRenderer(upperRightCornerRenderer, corner, getCorner(UPPER_RIGHT_CORNER)) { setCorner(UPPER_RIGHT_CORNER, it) }
		}
		props.lowerLeftCorner.ifPresent { corner ->
			updateRenderer(lowerLeftCornerRenderer, corner, getCorner(LOWER_LEFT_CORNER)) { setCorner(LOWER_LEFT_CORNER, it) }
		}
		props.lowerRightCorner.ifPresent { corner ->
			updateRenderer(lowerRightCornerRenderer, corner, getCorner(LOWER_RIGHT_CORNER)) { setCorner(LOWER_RIGHT_CORNER, it) }
		}
	}

	private fun updateRenderer(renderer: EmbeddedRenderer, element: SwingElement<*>?,
	                           currentComponent: Component?, setter: (Component?) -> Unit)
	{
		if(element != null)
		{
			renderer.update(element)
			val component = renderer.component
			if(currentComponent != component) setter(component)
		}
		else if(currentComponent != null) setter(null)
	}
}

fun SwingRenderScope.scrollPaneUnidirectional(key: Any = AutoKey) =
		scrollPaneUnidirectional(props = SwingScrollPaneUnidirectional.Props(key))

internal fun SwingRenderScope.scrollPaneUnidirectional(nativeComponent: () -> JScrollPane = ::JScrollPane,
                                                       props: SwingScrollPaneUnidirectional.Props) =
		component({ SwingScrollPaneUnidirectional(nativeComponent(), it) }, props)

private typealias SSPUProvider<P> = SwingScrollPaneUnidirectional.PropsProvider<P>
fun <P : SSPUProvider<P>> SwingElement<P>.withScrollPaneUnidirectionalProps(builder: Builder<SwingScrollPaneUnidirectional.Props>) =
		withProps { withScrollPaneUnidirectionalProps(builder) }
fun <P : SSPUProvider<P>> SwingElement<P>.upperLeftCorner(corner: SwingRenderScope.() -> SwingElement<*>) =
		withScrollPaneUnidirectionalProps { copy(upperLeftCorner = corner.render().prop()) }
fun <P : SSPUProvider<P>> SwingElement<P>.upperRightCorner(corner: SwingRenderScope.() -> SwingElement<*>) =
		withScrollPaneUnidirectionalProps { copy(upperRightCorner = corner.render().prop()) }
fun <P : SSPUProvider<P>> SwingElement<P>.lowerLeftCorner(corner: SwingRenderScope.() -> SwingElement<*>) =
		withScrollPaneUnidirectionalProps { copy(lowerLeftCorner = corner.render().prop()) }
fun <P : SSPUProvider<P>> SwingElement<P>.lowerRightCorner(corner: SwingRenderScope.() -> SwingElement<*>) =
		withScrollPaneUnidirectionalProps { copy(lowerRightCorner = corner.render().prop()) }
