package pl.karol202.uranium.swing.control.text

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.event.ActionListener
import javax.swing.JPasswordField

class SwingPasswordField(private val nativeComponent: JPasswordField,
                         initialProps: Props) : SwingAbstractAppComponent<SwingPasswordField.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val textFieldProps: SwingTextField.Props = SwingTextField.Props(),
	                 val echoChar: Prop<Char?> = Prop.NoValue,
	                 val onPasswordApply: Prop<(CharArray) -> Unit> = Prop.NoValue) : UProps,
	                                                                                  SwingNativeComponent.PropsProvider<Props>,
	                                                                                  SwingAbstractTextComponent.PropsProvider<Props>,
	                                                                                  SwingTextField.PropsProvider<Props>,
	                                                                                  PropsProvider<Props>
	{
		override val swingProps = textFieldProps.swingProps
		override val abstractTextProps = textFieldProps.abstractTextProps
		override val passwordFieldProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(textFieldProps = textFieldProps.withSwingProps(builder))

		override fun withAbstractTextProps(builder: Builder<SwingAbstractTextComponent.Props>) =
				copy(textFieldProps = textFieldProps.withAbstractTextProps(builder))

		override fun withTextFieldProps(builder: Builder<SwingTextField.Props>) =
				copy(textFieldProps = textFieldProps.builder())

		override fun withPasswordFieldProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val passwordFieldProps: Props

		fun withPasswordFieldProps(builder: Builder<Props>): S
	}

	companion object
	{
		const val PASSWORD_VISIBLE_CHAR = 0
	}

	private val actionListener = ActionListener { props.onPasswordApply.value?.invoke(nativeComponent.password) }

	override fun onCreate() = nativeComponent.update {
		addActionListener(actionListener)
	}

	override fun onDestroy() = nativeComponent.update {
		removeActionListener(actionListener)
	}

	override fun URenderScope<Swing>.render() =
			textField(nativeComponent = { nativeComponent }, props = props.textFieldProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
		props.echoChar.ifPresent { echoChar = it ?: PASSWORD_VISIBLE_CHAR.toChar() }
	}
}

fun SwingRenderScope.passwordField(key: Any = AutoKey) = passwordField(props = SwingPasswordField.Props(key))

internal fun SwingRenderScope.passwordField(nativeComponent: () -> JPasswordField = ::JPasswordField,
                                            props: SwingPasswordField.Props) =
		component({ SwingPasswordField(nativeComponent(), it) }, props)

private typealias SPFProvider<P> = SwingPasswordField.PropsProvider<P>
fun <P : SPFProvider<P>> SwingElement<P>.withPasswordFieldProps(builder: Builder<SwingPasswordField.Props>) =
		withProps { withPasswordFieldProps(builder) }
fun <P : SPFProvider<P>> SwingElement<P>.echoChar(char: Char?) =
		withPasswordFieldProps { copy(echoChar = char.prop()) }
fun <P : SPFProvider<P>> SwingElement<P>.onPasswordApply(listener: (CharArray) -> Unit) =
		withPasswordFieldProps { copy(onPasswordApply = listener.prop()) }
