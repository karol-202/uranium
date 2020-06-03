package pl.karol202.uranium.swing.layout.border

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.core.render.render
import pl.karol202.uranium.swing.layout.constraint
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.BorderLayout
import java.awt.BorderLayout.*

class SwingBorderLayoutCompass(initialProps: Props) : SwingAbstractAppComponent<SwingBorderLayoutCompass.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val borderLayoutBaseProps: SwingBorderLayoutBase.Props = SwingBorderLayoutBase.Props(),
	                 val center: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val north: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val west: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val east: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val south: Prop<SwingElement<*>?> = Prop.NoValue) : UProps,
	                                                                     SwingNativeComponent.PropsProvider<Props>,
	                                                                     SwingBorderLayoutBase.PropsProvider<Props>,
	                                                                     PropsProvider<Props>
	{
		override val swingProps = borderLayoutBaseProps.swingProps
		override val borderLayoutCompassProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(borderLayoutBaseProps = borderLayoutBaseProps.withSwingProps(builder))

		override fun withBorderLayoutBaseProps(builder: Builder<SwingBorderLayoutBase.Props>) =
				copy(borderLayoutBaseProps = borderLayoutBaseProps.builder())

		override fun withBorderLayoutCompassProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val borderLayoutCompassProps: Props

		fun withBorderLayoutCompassProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() =
			borderLayoutBase(props = props.borderLayoutBaseProps.copy(content = renderContent()))

	private fun SwingRenderScope.renderContent() = listOf(CENTER to props.center,
	                                                      NORTH to props.north,
	                                                      WEST to props.west,
	                                                      EAST to props.east,
	                                                      SOUTH to props.south)
			.mapNotNull { (side, elementProp) -> elementProp.value?.let { side to it } }
			.map { (side, element) -> constraint(constraint = side, content = element) }
}

fun SwingRenderScope.borderLayoutCompass(key: Any = AutoKey) = borderLayoutCompass(props = SwingBorderLayoutCompass.Props(key))

internal fun SwingRenderScope.borderLayoutCompass(props: SwingBorderLayoutCompass.Props) =
		component(::SwingBorderLayoutCompass, props)

private typealias SBLCProvider<P> = SwingBorderLayoutCompass.PropsProvider<P>
fun <P : SBLCProvider<P>> SwingElement<P>.withBorderLayoutCompassProps(builder: Builder<SwingBorderLayoutCompass.Props>) =
		withProps { withBorderLayoutCompassProps(builder) }
fun <P : SBLCProvider<P>> SwingElement<P>.center(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutCompassProps { copy(center = element.render().prop()) }
fun <P : SBLCProvider<P>> SwingElement<P>.north(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutCompassProps { copy(north = element.render().prop()) }
fun <P : SBLCProvider<P>> SwingElement<P>.west(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutCompassProps { copy(west = element.render().prop()) }
fun <P : SBLCProvider<P>> SwingElement<P>.east(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutCompassProps { copy(east = element.render().prop()) }
fun <P : SBLCProvider<P>> SwingElement<P>.south(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutCompassProps { copy(south = element.render().prop()) }
