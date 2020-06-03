package pl.karol202.uranium.swing.control.progress

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import javax.swing.JProgressBar

class SwingProgressBar(private val nativeComponent: JProgressBar,
                       initialProps: Props) : SwingAbstractAppComponent<SwingProgressBar.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val value: Prop<Int> = Prop.NoValue,
	                 val minimum: Prop<Int> = Prop.NoValue,
	                 val maximum: Prop<Int> = Prop.NoValue,
	                 val indeterminate: Prop<Boolean> = Prop.NoValue,
	                 val borderPainted: Prop<Boolean> = Prop.NoValue,
	                 val stringPainted: Prop<Boolean> = Prop.NoValue,
	                 val orientation: Prop<Orientation> = Prop.NoValue,
	                 val string: Prop<String?> = Prop.NoValue) : UProps,
	                                                             SwingNativeComponent.PropsProvider<Props>,
	                                                             PropsProvider<Props>
	{
		override val progressBarProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(swingProps = swingProps.builder())

		override fun withProgressBarProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val progressBarProps: Props

		fun withProgressBarProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { nativeComponent }, props = props.swingProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
		props.value.ifPresent { value = it }
		props.minimum.ifPresent { minimum = it }
		props.maximum.ifPresent { maximum = it }
		props.indeterminate.ifPresent { isIndeterminate = it }
		props.borderPainted.ifPresent { isBorderPainted = it }
		props.stringPainted.ifPresent { isStringPainted = it }
		props.orientation.ifPresent { orientation = it.code }
		props.string.ifPresent { string = it }
	}
}

fun SwingRenderScope.progressBar(key: Any = AutoKey) = progressBar(props = SwingProgressBar.Props(key))

internal fun SwingRenderScope.progressBar(nativeComponent: () -> JProgressBar = ::JProgressBar,
                                          props: SwingProgressBar.Props) =
		component({ SwingProgressBar(nativeComponent(), it) }, props)

private typealias SPBProvider<P> = SwingProgressBar.PropsProvider<P>
fun <P : SPBProvider<P>> SwingElement<P>.withProgressBarProps(builder: Builder<SwingProgressBar.Props>) =
		withProps { withProgressBarProps(builder) }
fun <P : SPBProvider<P>> SwingElement<P>.value(value: Int) =
		withProgressBarProps { copy(value = value.prop()) }
fun <P : SPBProvider<P>> SwingElement<P>.minimum(minimum: Int) =
		withProgressBarProps { copy(minimum = minimum.prop()) }
fun <P : SPBProvider<P>> SwingElement<P>.maximum(maximum: Int) =
		withProgressBarProps { copy(maximum = maximum.prop()) }
fun <P : SPBProvider<P>> SwingElement<P>.indeterminate(indeterminate: Boolean) =
		withProgressBarProps { copy(indeterminate = indeterminate.prop()) }
fun <P : SPBProvider<P>> SwingElement<P>.borderPainted(border: Boolean) =
		withProgressBarProps { copy(borderPainted = border.prop()) }
fun <P : SPBProvider<P>> SwingElement<P>.stringPainted(string: Boolean) =
		withProgressBarProps { copy(stringPainted = string.prop()) }
fun <P : SPBProvider<P>> SwingElement<P>.orientation(orientation: Orientation) =
		withProgressBarProps { copy(orientation = orientation.prop()) }
fun <P : SPBProvider<P>> SwingElement<P>.string(string: String) =
		withProgressBarProps { copy(string = string.prop()) }
