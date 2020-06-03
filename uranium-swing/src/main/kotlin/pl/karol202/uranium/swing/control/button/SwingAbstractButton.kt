package pl.karol202.uranium.swing.control.button

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.Insets
import java.awt.event.ActionListener
import javax.swing.AbstractButton
import javax.swing.Icon

class SwingAbstractButton(private val nativeComponent: AbstractButton,
                          initialProps: Props) : SwingAbstractAppComponent<SwingAbstractButton.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val text: Prop<String?> = Prop.NoValue,
	                 val icon: Prop<Icon?> = Prop.NoValue,
	                 val pressedIcon: Prop<Icon?> = Prop.NoValue,
	                 val selectedIcon: Prop<Icon?> = Prop.NoValue,
	                 val rolloverIcon: Prop<Icon?> = Prop.NoValue,
	                 val rolloverSelectedIcon: Prop<Icon?> = Prop.NoValue,
	                 val disabledIcon: Prop<Icon?> = Prop.NoValue,
	                 val disabledSelectedIcon: Prop<Icon?> = Prop.NoValue,
	                 val iconTextGap: Prop<Int> = Prop.NoValue,
	                 val borderPainted: Prop<Boolean> = Prop.NoValue,
	                 val contentAreaFilled: Prop<Boolean> = Prop.NoValue,
	                 val focusPainted: Prop<Boolean> = Prop.NoValue,
	                 val rolloverEnabled: Prop<Boolean> = Prop.NoValue,
	                 val selected: Prop<Boolean> = Prop.NoValue,
	                 val horizontalAlign: Prop<HorizontalAlign> = Prop.NoValue,
	                 val verticalAlign: Prop<VerticalAlign> = Prop.NoValue,
	                 val horizontalTextPosition: Prop<HorizontalAlign> = Prop.NoValue,
	                 val verticalTextPosition: Prop<VerticalAlign> = Prop.NoValue,
	                 val margin: Prop<Insets?> = Prop.NoValue,
	                 val multiClickThreshold: Prop<Long> = Prop.NoValue,
	                 val mnemonic: Prop<Int> = Prop.NoValue,
	                 val displayedMnemonicIndex: Prop<Int> = Prop.NoValue,
	                 val onClick: Prop<() -> Unit> = Prop.NoValue) : UProps,
	                                                                 SwingNativeComponent.PropsProvider<Props>,
	                                                                 PropsProvider<Props>
	{
		override val abstractButtonProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())

		override fun withAbstractButtonProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val abstractButtonProps: Props

		fun withAbstractButtonProps(builder: Builder<Props>): S
	}

	private val actionListener = ActionListener { onClick() }

	override fun onCreate() = nativeComponent.update {
		addActionListener(actionListener)
	}

	override fun onDestroy() = nativeComponent.update {
		removeActionListener(actionListener)
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { nativeComponent }, props = props.swingProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
		props.text.ifPresent { text = it }
		props.icon.ifPresent { icon = it }
		props.pressedIcon.ifPresent { pressedIcon = it }
		props.selectedIcon.ifPresent { selectedIcon = it }
		props.rolloverIcon.ifPresent { rolloverIcon = it }
		props.rolloverSelectedIcon.ifPresent { rolloverSelectedIcon = it }
		props.disabledIcon.ifPresent { disabledIcon = it }
		props.disabledSelectedIcon.ifPresent { disabledSelectedIcon = it }
		props.iconTextGap.ifPresent { iconTextGap = it }
		props.borderPainted.ifPresent { isBorderPainted = it }
		props.contentAreaFilled.ifPresent { isContentAreaFilled = it }
		props.focusPainted.ifPresent { isFocusPainted = it }
		props.rolloverEnabled.ifPresent { isRolloverEnabled = it }
		props.selected.ifPresent { isSelected = it }
		props.horizontalAlign.ifPresent { horizontalAlignment = it.code }
		props.verticalAlign.ifPresent { verticalAlignment = it.code }
		props.horizontalTextPosition.ifPresent { horizontalTextPosition = it.code }
		props.verticalTextPosition.ifPresent { verticalTextPosition = it.code }
		props.margin.ifPresent { margin = it }
		props.multiClickThreshold.ifPresent { multiClickThreshhold = it }
		props.mnemonic.ifPresent { mnemonic = it }
		props.displayedMnemonicIndex.ifPresent { displayedMnemonicIndex = it }
	}

	private fun onClick()
	{
		props.onClick.value?.invoke()
		invalidate()
	}
}

internal fun SwingRenderScope.abstractButton(nativeComponent: () -> AbstractButton,
                                             props: SwingAbstractButton.Props) =
		component({ SwingAbstractButton(nativeComponent(), it) }, props)

private typealias SABProvider<P> = SwingAbstractButton.PropsProvider<P>
fun <P : SABProvider<P>> SwingElement<P>.withAbstractButtonProps(builder: Builder<SwingAbstractButton.Props>) = withProps { withAbstractButtonProps(builder) }
fun <P : SABProvider<P>> SwingElement<P>.text(text: String?) = withAbstractButtonProps { copy(text = text.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.icon(icon: Icon?) = withAbstractButtonProps { copy(icon = icon.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.pressedIcon(icon: Icon?) = withAbstractButtonProps { copy(pressedIcon = icon.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.selectedIcon(icon: Icon?) = withAbstractButtonProps { copy(selectedIcon = icon.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.rolloverIcon(icon: Icon?) = withAbstractButtonProps { copy(rolloverIcon = icon.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.rolloverSelectedIcon(icon: Icon?) = withAbstractButtonProps { copy(rolloverSelectedIcon = icon.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.disabledIcon(icon: Icon?) = withAbstractButtonProps { copy(disabledIcon = icon.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.disabledSelectedIcon(icon: Icon?) = withAbstractButtonProps { copy(disabledSelectedIcon = icon.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.iconTextGap(gap: Int) = withAbstractButtonProps { copy(iconTextGap = gap.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.borderPainted(painted: Boolean) = withAbstractButtonProps { copy(borderPainted = painted.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.contentAreaFilled(filled: Boolean) = withAbstractButtonProps { copy(contentAreaFilled = filled.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.focusPainted(painted: Boolean) = withAbstractButtonProps { copy(focusPainted = painted.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.rolloverEnabled(enabled: Boolean) = withAbstractButtonProps { copy(rolloverEnabled = enabled.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.selected(selected: Boolean) = withAbstractButtonProps { copy(selected = selected.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.horizontalAlign(align: HorizontalAlign) = withAbstractButtonProps { copy(horizontalAlign = align.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.verticalAlign(align: VerticalAlign) = withAbstractButtonProps { copy(verticalAlign = align.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.horizontalTextPosition(position: HorizontalAlign) = withAbstractButtonProps { copy(horizontalTextPosition = position.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.verticalTextPosition(position: VerticalAlign) = withAbstractButtonProps { copy(verticalTextPosition = position.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.margin(margin: Insets?) = withAbstractButtonProps { copy(margin = margin.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.multiClickThreshold(threshold: Long) = withAbstractButtonProps { copy(multiClickThreshold = threshold.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.mnemonic(mnemonic: Int) = withAbstractButtonProps { copy(mnemonic = mnemonic.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.displayedMnemonicIndex(index: Int) = withAbstractButtonProps { copy(displayedMnemonicIndex = index.prop()) }
fun <P : SABProvider<P>> SwingElement<P>.onClick(onClick: () -> Unit) = withAbstractButtonProps { copy(onClick = onClick.prop()) }
