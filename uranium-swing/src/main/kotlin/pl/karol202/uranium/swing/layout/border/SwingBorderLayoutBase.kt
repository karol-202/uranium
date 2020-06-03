package pl.karol202.uranium.swing.layout.border

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.layout.SwingLayout
import pl.karol202.uranium.swing.layout.layout
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.BorderLayout
import java.awt.Container
import java.awt.LayoutManager

class SwingBorderLayoutBase(initialProps: Props) : SwingAbstractAppComponent<SwingBorderLayoutBase.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val content: List<SwingElement<*>> = emptyList(),
	                 val horizontalGap: Prop<Int> = Prop.NoValue,
	                 val verticalGap: Prop<Int> = Prop.NoValue) : UProps,
	                                                              SwingNativeComponent.PropsProvider<Props>,
	                                                              PropsProvider<Props>
	{
		override val borderLayoutBaseProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(swingProps = swingProps.builder())

		override fun withBorderLayoutBaseProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val borderLayoutBaseProps: Props

		fun withBorderLayoutBaseProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() = layout(swingProps = props.swingProps,
	                                                   content = props.content,
	                                                   layoutUpdater = ::updateLayout)

	private fun updateLayout(container: Container, layout: LayoutManager?) = (layout as? BorderLayout ?: BorderLayout()).apply {
		props.horizontalGap.ifPresent { hgap = it }
		props.verticalGap.ifPresent { vgap = it }
	}
}

internal fun SwingRenderScope.borderLayoutBase(props: SwingBorderLayoutBase.Props) = component(::SwingBorderLayoutBase, props)

private typealias SBLProvider<P> = SwingBorderLayoutBase.PropsProvider<P>
fun <P : SBLProvider<P>> SwingElement<P>.withBorderLayoutBaseProps(builder: Builder<SwingBorderLayoutBase.Props>) = withProps { withBorderLayoutBaseProps(builder) }
fun <P : SBLProvider<P>> SwingElement<P>.horizontalGap(gap: Int) = withBorderLayoutBaseProps { copy(horizontalGap = gap.prop()) }
fun <P : SBLProvider<P>> SwingElement<P>.verticalGap(gap: Int) = withBorderLayoutBaseProps { copy(verticalGap = gap.prop()) }
