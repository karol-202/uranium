package pl.karol202.uranium.swing.control.text

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import javax.swing.JTextArea

class SwingTextArea(private val nativeComponent: JTextArea,
                    initialProps: Props) : SwingAbstractAppComponent<SwingTextArea.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val abstractTextProps: SwingAbstractTextComponent.Props = SwingAbstractTextComponent.Props(),
	                 val columns: Prop<Int> = Prop.NoValue,
	                 val rows: Prop<Int> = Prop.NoValue,
	                 val lineWrap: Prop<Boolean> = Prop.NoValue,
	                 val tabSize: Prop<Int> = Prop.NoValue,
	                 val wrapOnWords: Prop<Boolean> = Prop.NoValue) : UProps,
	                                                                  SwingNativeComponent.PropsProvider<Props>,
	                                                                  SwingAbstractTextComponent.PropsProvider<Props>,
	                                                                  PropsProvider<Props>
	{
		override val swingProps = abstractTextProps.swingProps
		override val textAreaProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(abstractTextProps = abstractTextProps.withSwingProps(builder))

		override fun withAbstractTextProps(builder: Builder<SwingAbstractTextComponent.Props>) =
				copy(abstractTextProps = abstractTextProps.builder())

		override fun withTextAreaProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val textAreaProps: Props

		fun withTextAreaProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() =
			abstractTextComponent(nativeComponent = { nativeComponent }, props = props.abstractTextProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
		props.columns.ifPresent { columns = it }
		props.rows.ifPresent { rows = it }
		props.lineWrap.ifPresent { lineWrap = it }
		props.tabSize.ifPresent { tabSize = it }
		props.wrapOnWords.ifPresent { wrapStyleWord = it }
	}
}

fun SwingRenderScope.textArea(key: Any = AutoKey) = textArea(props = SwingTextArea.Props(key))

internal fun SwingRenderScope.textArea(nativeComponent: () -> JTextArea = ::JTextArea,
                                       props: SwingTextArea.Props) =
		component({ SwingTextArea(nativeComponent(), it) }, props)

private typealias STAProvider<P> = SwingTextArea.PropsProvider<P>
fun <P : STAProvider<P>> SwingElement<P>.withTextAreaProps(builder: Builder<SwingTextArea.Props>) = withProps { withTextAreaProps(builder) }
fun <P : STAProvider<P>> SwingElement<P>.columns(columns: Int) = withTextAreaProps { copy(columns = columns.prop()) }
fun <P : STAProvider<P>> SwingElement<P>.rows(rows: Int) = withTextAreaProps { copy(rows = rows.prop()) }
fun <P : STAProvider<P>> SwingElement<P>.lineWrap(wrap: Boolean) = withTextAreaProps { copy(lineWrap = wrap.prop()) }
fun <P : STAProvider<P>> SwingElement<P>.tabSize(tabSize: Int) = withTextAreaProps { copy(tabSize = tabSize.prop()) }
fun <P : STAProvider<P>> SwingElement<P>.wrapOnWords(wrap: Boolean) = withTextAreaProps { copy(wrapOnWords = wrap.prop()) }
