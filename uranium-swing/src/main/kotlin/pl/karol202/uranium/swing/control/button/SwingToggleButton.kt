package pl.karol202.uranium.swing.control.button

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.JToggleButton

class SwingToggleButton(private val nativeComponent: JToggleButton,
                        initialProps: Props) : SwingAbstractAppComponent<SwingToggleButton.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val abstractButtonProps: SwingAbstractButton.Props = SwingAbstractButton.Props(),
	                 val onSelect: Prop<(Boolean) -> Unit> = Prop.NoValue) : UProps,
	                                                                         SwingNativeComponent.PropsProvider<Props>,
	                                                                         SwingAbstractButton.PropsProvider<Props>,
	                                                                         PropsProvider<Props>
	{
		override val swingProps = abstractButtonProps.swingProps
		override val toggleButtonProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(abstractButtonProps = abstractButtonProps.withSwingProps(builder))

		override fun withAbstractButtonProps(builder: Builder<SwingAbstractButton.Props>) =
				copy(abstractButtonProps = abstractButtonProps.builder())

		override fun withToggleButtonProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val toggleButtonProps: Props

		fun withToggleButtonProps(builder: Builder<Props>): S
	}

	private val itemListener = ItemListener { onSelect(it.stateChange == ItemEvent.SELECTED) }

	override fun onCreate() = nativeComponent.update {
		addItemListener(itemListener)
	}

	override fun onDestroy() = nativeComponent.update {
		removeItemListener(itemListener)
	}

	override fun URenderScope<Swing>.render() =
			abstractButton(nativeComponent = { nativeComponent }, props = props.abstractButtonProps)

	private fun onSelect(selected: Boolean)
	{
		if(selected != props.abstractButtonProps.selected.value) props.onSelect.value?.invoke(selected)
		invalidate()
	}
}

fun SwingRenderScope.toggleButton(key: Any = AutoKey) = toggleButton(props = SwingToggleButton.Props(key))

internal fun SwingRenderScope.toggleButton(nativeComponent: () -> JToggleButton = ::JToggleButton,
                                           props: SwingToggleButton.Props) =
		component({ SwingToggleButton(nativeComponent(), it) }, props)

private typealias STBProvider<P> = SwingToggleButton.PropsProvider<P>
fun <P : STBProvider<P>> SwingElement<P>.withToggleButtonProps(builder: Builder<SwingToggleButton.Props>) = withProps { withToggleButtonProps(builder) }
fun <P : STBProvider<P>> SwingElement<P>.onSelect(onSelect: (Boolean) -> Unit) = withToggleButtonProps { copy(onSelect = onSelect.prop()) }
