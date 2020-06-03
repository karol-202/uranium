package pl.karol202.uranium.swing.control.label

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import javax.swing.Icon
import javax.swing.JLabel

class SwingLabel(private val nativeComponent: JLabel,
                 initialProps: Props) : SwingAbstractAppComponent<SwingLabel.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val text: Prop<String?> = Prop.NoValue,
	                 val icon: Prop<Icon?> = Prop.NoValue,
	                 val disabledIcon: Prop<Icon?> = Prop.NoValue,
	                 val iconTextGap: Prop<Int> = Prop.NoValue,
	                 val horizontalAlign: Prop<HorizontalAlign> = Prop.NoValue,
	                 val verticalAlign: Prop<VerticalAlign> = Prop.NoValue,
	                 val horizontalTextPosition: Prop<HorizontalAlign> = Prop.NoValue,
	                 val verticalTextPosition: Prop<VerticalAlign> = Prop.NoValue) : UProps,
	                                                                                 SwingNativeComponent.PropsProvider<Props>,
	                                                                                 PropsProvider<Props>
	{
		override val labelProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(swingProps = swingProps.builder())

		override fun withLabelProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val labelProps: Props

		fun withLabelProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { nativeComponent }, props = props.swingProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
		props.text.ifPresent { text = it }
		props.icon.ifPresent { icon = it }
		props.disabledIcon.ifPresent { disabledIcon = it }
		props.iconTextGap.ifPresent { iconTextGap = it }
		props.horizontalAlign.ifPresent { horizontalAlignment = it.code }
		props.verticalAlign.ifPresent { verticalAlignment = it.code }
		props.horizontalTextPosition.ifPresent { horizontalTextPosition = it.code }
		props.verticalTextPosition.ifPresent { verticalTextPosition = it.code }
	}
}

fun SwingRenderScope.label(key: Any = AutoKey) = label(props = SwingLabel.Props(key))

internal fun SwingRenderScope.label(nativeComponent: () -> JLabel = ::JLabel,
                                    props: SwingLabel.Props) =
		component({ SwingLabel(nativeComponent(), it) }, props)

private typealias SLProvider<P> = SwingLabel.PropsProvider<P>
fun <P : SLProvider<P>> SwingElement<P>.withLabelProps(builder: Builder<SwingLabel.Props>) = withProps { withLabelProps(builder) }
fun <P : SLProvider<P>> SwingElement<P>.text(text: String?) = withLabelProps { copy(text = text.prop()) }
fun <P : SLProvider<P>> SwingElement<P>.icon(icon: Icon?) = withLabelProps { copy(icon = icon.prop()) }
fun <P : SLProvider<P>> SwingElement<P>.disabledIcon(icon: Icon?) = withLabelProps { copy(disabledIcon = icon.prop()) }
fun <P : SLProvider<P>> SwingElement<P>.iconTextGap(gap: Int) = withLabelProps { copy(iconTextGap = gap.prop()) }
fun <P : SLProvider<P>> SwingElement<P>.horizontalAlign(align: HorizontalAlign) = withLabelProps { copy(horizontalAlign = align.prop()) }
fun <P : SLProvider<P>> SwingElement<P>.verticalAlign(align: VerticalAlign) = withLabelProps { copy(verticalAlign = align.prop()) }
fun <P : SLProvider<P>> SwingElement<P>.horizontalTextPosition(position: HorizontalAlign) = withLabelProps { copy(horizontalTextPosition = position.prop()) }
fun <P : SLProvider<P>> SwingElement<P>.verticalTextPosition(position: VerticalAlign) = withLabelProps { copy(verticalTextPosition = position.prop()) }
