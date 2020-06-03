package pl.karol202.uranium.swing.layout.flow

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.core.render.render
import pl.karol202.uranium.swing.layout.SwingLayout
import pl.karol202.uranium.swing.layout.layout
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.Container
import java.awt.FlowLayout
import java.awt.LayoutManager

class SwingFlowLayout(initialProps: Props) : SwingAbstractAppComponent<SwingFlowLayout.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val content: List<SwingElement<*>> = emptyList(),
	                 val align: Prop<FlowAlign> = Prop.NoValue,
	                 val alignOnBaseline: Prop<Boolean> = Prop.NoValue,
	                 val horizontalGap: Prop<Int> = Prop.NoValue,
	                 val verticalGap: Prop<Int> = Prop.NoValue) : UProps,
	                                                              SwingNativeComponent.PropsProvider<Props>,
	                                                              PropsProvider<Props>
	{
		override val flowLayoutProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(swingProps = swingProps.builder())

		override fun withFlowLayoutProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val flowLayoutProps: Props

		fun withFlowLayoutProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() = layout(swingProps = props.swingProps,
	                                                   content = props.content,
	                                                   layoutUpdater = ::updateLayout)

	private fun updateLayout(container: Container, layout: LayoutManager?) = (layout as? FlowLayout ?: FlowLayout()).apply {
		props.align.ifPresent { alignment = it.code }
		props.alignOnBaseline.ifPresent { alignOnBaseline = it }
		props.horizontalGap.ifPresent { hgap = it }
		props.verticalGap.ifPresent { vgap = it }
	}
}

fun SwingRenderScope.flowLayout(key: Any = AutoKey,
                                block: SwingRenderBuilder.() -> Unit = {}) =
		flowLayout(props = SwingFlowLayout.Props(key)).content(block)

internal fun SwingRenderScope.flowLayout(props: SwingFlowLayout.Props) = component(::SwingFlowLayout, props)

private typealias SFLProvider<P> = SwingFlowLayout.PropsProvider<P>
fun <P : SFLProvider<P>> SwingElement<P>.withFlowLayoutProps(builder: Builder<SwingFlowLayout.Props>) = withProps { withFlowLayoutProps(builder) }
fun <P : SFLProvider<P>> SwingElement<P>.align(align: FlowAlign) = withFlowLayoutProps { copy(align = align.prop()) }
fun <P : SFLProvider<P>> SwingElement<P>.alignOnBaseline(align: Boolean) = withFlowLayoutProps { copy(alignOnBaseline = align.prop()) }
fun <P : SFLProvider<P>> SwingElement<P>.horizontalGap(gap: Int) = withFlowLayoutProps { copy(horizontalGap = gap.prop()) }
fun <P : SFLProvider<P>> SwingElement<P>.verticalGap(gap: Int) = withFlowLayoutProps { copy(verticalGap = gap.prop()) }
fun <P : SFLProvider<P>> SwingElement<P>.content(block: SwingRenderBuilder.() -> Unit) = withFlowLayoutProps { copy(content = block.render()) }
