package pl.karol202.uranium.swing.layout.border

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.core.render.render
import pl.karol202.uranium.swing.layout.constraint
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.BorderLayout.*

class SwingBorderLayoutPage(initialProps: Props) : SwingAbstractAppComponent<SwingBorderLayoutPage.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val borderLayoutBaseProps: SwingBorderLayoutBase.Props = SwingBorderLayoutBase.Props(),
	                 val center: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val pageStart: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val lineStart: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val lineEnd: Prop<SwingElement<*>?> = Prop.NoValue,
	                 val pageEnd: Prop<SwingElement<*>?> = Prop.NoValue) : UProps,
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

		override fun withBorderLayoutPageProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val borderLayoutCompassProps: Props

		fun withBorderLayoutPageProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() =
			borderLayoutBase(props = props.borderLayoutBaseProps.copy(content = renderContent()))

	private fun SwingRenderScope.renderContent() = listOf(CENTER to props.center,
	                                                      PAGE_START to props.pageStart,
	                                                      LINE_START to props.lineStart,
	                                                      LINE_END to props.lineEnd,
	                                                      PAGE_END to props.pageEnd)
			.mapNotNull { (side, elementProp) -> elementProp.value?.let { side to it } }
			.map { (side, element) -> constraint(constraint = side, content = element) }
}

fun SwingRenderScope.borderLayout(key: Any = AutoKey) = borderLayoutPage(props = SwingBorderLayoutPage.Props(key))

internal fun SwingRenderScope.borderLayoutPage(props: SwingBorderLayoutPage.Props) = component(::SwingBorderLayoutPage, props)

private typealias SBLPProvider<P> = SwingBorderLayoutPage.PropsProvider<P>
fun <P : SBLPProvider<P>> SwingElement<P>.withBorderLayoutPageProps(builder: Builder<SwingBorderLayoutPage.Props>) =
		withProps { withBorderLayoutPageProps(builder) }
fun <P : SBLPProvider<P>> SwingElement<P>.center(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutPageProps { copy(center = element.render().prop()) }
fun <P : SBLPProvider<P>> SwingElement<P>.pageStart(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutPageProps { copy(pageStart = element.render().prop()) }
fun <P : SBLPProvider<P>> SwingElement<P>.lineStart(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutPageProps { copy(lineStart = element.render().prop()) }
fun <P : SBLPProvider<P>> SwingElement<P>.lineEnd(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutPageProps { copy(lineEnd = element.render().prop()) }
fun <P : SBLPProvider<P>> SwingElement<P>.pageEnd(element: SwingRenderScope.() -> SwingElement<*>?) =
		withBorderLayoutPageProps { copy(pageEnd = element.render().prop()) }
