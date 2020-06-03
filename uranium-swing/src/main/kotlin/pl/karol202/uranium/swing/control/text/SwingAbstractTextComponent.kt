package pl.karol202.uranium.swing.control.text

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.Color
import java.awt.Insets
import javax.swing.DropMode
import javax.swing.event.CaretListener
import javax.swing.text.*

class SwingAbstractTextComponent(private val nativeComponent: JTextComponent,
                                 initialProps: Props) : SwingAbstractAppComponent<SwingAbstractTextComponent.Props>(initialProps)
{
    data class Props(override val key: Any = AutoKey,
                     override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
                     val text: Prop<String?> = Prop.NoValue,
                     val caret: Prop<Caret> = Prop.NoValue,
                     val highlighter: Prop<Highlighter> = Prop.NoValue,
                     val keymap: Prop<Keymap> = Prop.NoValue,
                     val navigationFilter: Prop<NavigationFilter> = Prop.NoValue,
                     val selectionColor: Prop<Color> = Prop.NoValue,
                     val selectedTextColor: Prop<Color> = Prop.NoValue,
                     val disabledTextColor: Prop<Color> = Prop.NoValue,
                     val caretColor: Prop<Color> = Prop.NoValue,
                     val editable: Prop<Boolean> = Prop.NoValue,
                     val dragEnabled: Prop<Boolean> = Prop.NoValue,
                     val dropMode: Prop<DropMode> = Prop.NoValue,
                     val margin: Prop<Insets> = Prop.NoValue,
                     val focusAccelerator: Prop<Char> = Prop.NoValue,
                     val caretPosition: Prop<Int> = Prop.NoValue,
                     val selectionStart: Prop<Int> = Prop.NoValue,
                     val selectionEnd: Prop<Int> = Prop.NoValue,
                     val onCaretMove: Prop<(dot: Int, mark: Int) -> Unit> = Prop.NoValue,
                     val onTextChange: Prop<(String) -> Unit> = Prop.NoValue) : UProps,
                                                                                SwingNativeComponent.PropsProvider<Props>,
                                                                                PropsProvider<Props>
    {
        override val abstractTextProps = this

        override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())

        override fun withAbstractTextProps(builder: Builder<Props>) = builder()
    }

    interface PropsProvider<S : PropsProvider<S>> : UProps
    {
        val abstractTextProps: Props

        fun withAbstractTextProps(builder: Builder<Props>): S
    }

    private val caretListener = CaretListener { onCaretMove(dot = it.dot, mark = it.mark) }
    private val documentListener = TextChangeListenerDelegate { if(!ignoreTextChanges) onTextChange() else null }

    private var ignoreTextChanges = false

    override fun onCreate() = nativeComponent.update {
        addCaretListener(caretListener)
        document.addDocumentListener(documentListener)
    }

    override fun onDestroy() = nativeComponent.update {
        removeCaretListener(caretListener)
        document.removeDocumentListener(documentListener)
    }

    override fun URenderScope<Swing>.render() =
            nativeComponent(nativeComponent = { nativeComponent }, props = props.swingProps)

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
        ignoreTextChanges {
            props.text.ifPresent { if(it != text) text = it }
        }
        props.caret.ifPresent { caret = it }
        props.highlighter.ifPresent { highlighter = it }
        props.keymap.ifPresent { keymap = it }
        props.navigationFilter.ifPresent { navigationFilter = it }
        props.selectionColor.ifPresent { selectionColor = it }
        props.selectedTextColor.ifPresent { selectedTextColor = it }
        props.disabledTextColor.ifPresent { disabledTextColor = it }
        props.caretColor.ifPresent { caretColor = it }
        props.editable.ifPresent { isEditable = it }
        props.dragEnabled.ifPresent { dragEnabled = it }
        props.dropMode.ifPresent { dropMode = it }
    }

    private fun ignoreTextChanges(block: () -> Unit)
    {
        ignoreTextChanges = true
        block()
        ignoreTextChanges = false
    }

    private fun onCaretMove(dot: Int, mark: Int)
    {
        props.onCaretMove.value?.invoke(dot, mark)
    }

    // For security reasons (JPasswordField), native.text mustn't be called unless onTextChange is specified
    private fun onTextChange(): ((String) -> Unit)? =
            props.onTextChange.value?.let { onTextChange ->
                { value: String -> if(value != props.text.value) onTextChange(value) }
            }.also { invalidate() }
}

internal fun SwingRenderScope.abstractTextComponent(nativeComponent: () -> JTextComponent,
                                                    props: SwingAbstractTextComponent.Props) =
        component({ SwingAbstractTextComponent(nativeComponent(), it) }, props)

private typealias SATCProvider<P> = SwingAbstractTextComponent.PropsProvider<P>
fun <P : SATCProvider<P>> SwingElement<P>.withAbstractTextProps(builder: Builder<SwingAbstractTextComponent.Props>) =
        withProps { withAbstractTextProps(builder) }
fun <P : SATCProvider<P>> SwingElement<P>.text(text: String?) = withAbstractTextProps { copy(text = text.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.caret(caret: Caret) = withAbstractTextProps { copy(caret = caret.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.highlighter(highlighter: Highlighter) = withAbstractTextProps { copy(highlighter = highlighter.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.keymap(keymap: Keymap) = withAbstractTextProps { copy(keymap = keymap.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.navigationFilter(filter: NavigationFilter) = withAbstractTextProps { copy(navigationFilter = filter.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.selectionColor(color: Color) = withAbstractTextProps { copy(selectionColor = color.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.selectedTextColor(color: Color) = withAbstractTextProps { copy(selectedTextColor = color.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.disabledTextColor(color: Color) = withAbstractTextProps { copy(disabledTextColor = color.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.caretColor(color: Color) = withAbstractTextProps { copy(caretColor = color.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.editable(editable: Boolean) = withAbstractTextProps { copy(editable = editable.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.dragEnabled(enabled: Boolean) = withAbstractTextProps { copy(dragEnabled = enabled.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.dropMode(mode: DropMode) = withAbstractTextProps { copy(dropMode = mode.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.margin(margin: Insets) = withAbstractTextProps { copy(margin = margin.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.focusAccelerator(char: Char) = withAbstractTextProps { copy(focusAccelerator = char.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.caretPosition(position: Int) = withAbstractTextProps { copy(caretPosition = position.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.selectionStart(start: Int) = withAbstractTextProps { copy(selectionStart = start.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.selectionEnd(end: Int) = withAbstractTextProps { copy(selectionEnd = end.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.onCaretMove(onMove: (dot: Int, mark: Int) -> Unit) = withAbstractTextProps { copy(onCaretMove = onMove.prop()) }
fun <P : SATCProvider<P>> SwingElement<P>.onTextChange(onChange: (String) -> Unit) = withAbstractTextProps { copy(onTextChange = onChange.prop()) }
