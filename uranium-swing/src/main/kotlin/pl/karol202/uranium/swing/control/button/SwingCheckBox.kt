package pl.karol202.uranium.swing.control.button

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import javax.swing.JCheckBox

class SwingCheckBox(private val nativeComponent: JCheckBox,
                    initialProps: Props) : SwingAbstractAppComponent<SwingCheckBox.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val toggleButtonProps: SwingToggleButton.Props = SwingToggleButton.Props(),
	                 val borderPaintedFlat: Prop<Boolean> = Prop.NoValue) : UProps,
	                                                                        SwingNativeComponent.PropsProvider<Props>,
	                                                                        SwingAbstractButton.PropsProvider<Props>,
	                                                                        SwingToggleButton.PropsProvider<Props>,
	                                                                        PropsProvider<Props>
	{
		override val swingProps = toggleButtonProps.swingProps
		override val abstractButtonProps = toggleButtonProps.abstractButtonProps
		override val checkBoxProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(toggleButtonProps = toggleButtonProps.withSwingProps(builder))

		override fun withAbstractButtonProps(builder: Builder<SwingAbstractButton.Props>) =
				copy(toggleButtonProps = toggleButtonProps.withAbstractButtonProps(builder))

		override fun withToggleButtonProps(builder: Builder<SwingToggleButton.Props>) =
				copy(toggleButtonProps = toggleButtonProps.builder())

		override fun withCheckBoxProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val checkBoxProps: Props

		fun withCheckBoxProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() =
			toggleButton(nativeComponent = { nativeComponent }, props = props.toggleButtonProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
		props.borderPaintedFlat.ifPresent { isBorderPaintedFlat = it }
	}
}

fun SwingRenderScope.checkBox(key: Any = AutoKey) = checkBox(props = SwingCheckBox.Props(key))

internal fun SwingRenderScope.checkBox(nativeComponent: () -> JCheckBox = ::JCheckBox,
                                       props: SwingCheckBox.Props) =
		component({ SwingCheckBox(nativeComponent(), it) }, props)

private typealias SCBProvider<P> = SwingCheckBox.PropsProvider<P>
fun <P : SCBProvider<P>> SwingElement<P>.withCheckBoxProps(builder: Builder<SwingCheckBox.Props>) = withProps { withCheckBoxProps(builder) }
fun <P : SCBProvider<P>> SwingElement<P>.borderPaintedFlat(flat: Boolean) = withCheckBoxProps { copy(borderPaintedFlat = flat.prop()) }
