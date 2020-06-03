package pl.karol202.uranium.swing.control.scrollbar

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.event.AdjustmentListener
import javax.swing.JScrollBar

class SwingScrollBar(private val nativeComponent: JScrollBar,
                     initialProps: Props) : SwingAbstractAppComponent<SwingScrollBar.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val axis: Prop<ScrollBarAxis> = Prop.NoValue,
	                 val value: Prop<Int> = Prop.NoValue,
	                 val minimum: Prop<Int> = Prop.NoValue,
	                 val maximum: Prop<Int> = Prop.NoValue,
	                 val blockIncrement: Prop<Int> = Prop.NoValue,
	                 val unitIncrement: Prop<Int> = Prop.NoValue,
	                 val visibleAmount: Prop<Int> = Prop.NoValue,
	                 val onChange: Prop<(Int) -> Unit> = Prop.NoValue) : UProps,
	                                                                     SwingNativeComponent.PropsProvider<Props>,
	                                                                     PropsProvider<Props>
	{
		override val scrollBarProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())

		override fun withScrollBarProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val scrollBarProps: Props

		fun withScrollBarProps(builder: Builder<Props>): S
	}

	private val adjustmentListener = AdjustmentListener { onChange() }

	override fun onCreate() = nativeComponent.update {
		addAdjustmentListener(adjustmentListener)
	}

	override fun onDestroy() = nativeComponent.update {
		removeAdjustmentListener(adjustmentListener)
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { nativeComponent }, props = props.swingProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
		props.axis.ifPresent { orientation = it.code }
		props.value.ifPresent { value = it }
		props.minimum.ifPresent { minimum = it }
		props.maximum.ifPresent { maximum = it }
		props.blockIncrement.ifPresent { blockIncrement = it }
		props.unitIncrement.ifPresent { unitIncrement = it }
		props.visibleAmount.ifPresent { visibleAmount = it }
	}

	private fun onChange()
	{
		if(nativeComponent.value == props.value.value) return
		props.onChange.value?.invoke(nativeComponent.value)
		invalidate()
	}
}

fun SwingRenderScope.scrollBar(key: Any = AutoKey) = scrollBar(props = SwingScrollBar.Props(key))

internal fun SwingRenderScope.scrollBar(nativeComponent: () -> JScrollBar = ::JScrollBar,
                                        props: SwingScrollBar.Props) =
		component({ SwingScrollBar(nativeComponent(), it) }, props)

private typealias SSBProvider<P> = SwingScrollBar.PropsProvider<P>
fun <P : SSBProvider<P>> SwingElement<P>.withScrollBarProps(builder: Builder<SwingScrollBar.Props>) =
		withProps { withScrollBarProps(builder) }
fun <P : SSBProvider<P>> SwingElement<P>.axis(axis: ScrollBarAxis) = withScrollBarProps { copy(axis = axis.prop()) }
fun <P : SSBProvider<P>> SwingElement<P>.value(value: Int) = withScrollBarProps { copy(value = value.prop()) }
fun <P : SSBProvider<P>> SwingElement<P>.minimum(minimum: Int) = withScrollBarProps { copy(minimum = minimum.prop()) }
fun <P : SSBProvider<P>> SwingElement<P>.maximum(maximum: Int) = withScrollBarProps { copy(maximum = maximum.prop()) }
fun <P : SSBProvider<P>> SwingElement<P>.blockIncrement(increment: Int) = withScrollBarProps { copy(blockIncrement = increment.prop()) }
fun <P : SSBProvider<P>> SwingElement<P>.unitIncrement(increment: Int) = withScrollBarProps { copy(unitIncrement = increment.prop()) }
fun <P : SSBProvider<P>> SwingElement<P>.visibleAmount(amount: Int) = withScrollBarProps { copy(visibleAmount = amount.prop()) }
fun <P : SSBProvider<P>> SwingElement<P>.onChange(onChange: (Int) -> Unit) = withScrollBarProps { copy(onChange = onChange.prop()) }
