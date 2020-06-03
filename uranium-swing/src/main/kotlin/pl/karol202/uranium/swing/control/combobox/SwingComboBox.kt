package pl.karol202.uranium.swing.control.combobox

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.control.list.CustomListCellRenderer
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import javax.swing.JComboBox

class SwingComboBox<E>(private val nativeComponent: JComboBox<E>,
                       initialProps: Props<E>) : SwingAbstractAppComponent<SwingComboBox.Props<E>>(initialProps)
{
	data class Props<E>(override val key: Any = AutoKey,
	                    override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                    val items: Prop<List<E>> = Prop.NoValue,
	                    val selectedItem: Prop<E?> = Prop.NoValue,
	                    val renderer: Prop<SwingRenderScope.(CustomListCellRenderer.Props<E>) -> SwingElement<*>> = Prop.NoValue,
	                    val editor: Prop<SwingRenderScope.(CustomComboBoxEditor.Props<E>) -> SwingElement<*>> = Prop.NoValue,
	                    val editable: Prop<Boolean> = Prop.NoValue,
	                    val popupVisible: Prop<Boolean> = Prop.NoValue,
	                    val lightweightPopup: Prop<Boolean> = Prop.NoValue,
	                    val maximumRowCount: Prop<Int> = Prop.NoValue,
	                    val prototypeDisplayValue: Prop<E?> = Prop.NoValue,
	                    val onSelect: Prop<(E) -> Unit> = Prop.NoValue,
	                    val onPopupShow: Prop<() -> Unit> = Prop.NoValue,
	                    val onPopupHide: Prop<() -> Unit> = Prop.NoValue,
	                    val onPopupCancel: Prop<() -> Unit> = Prop.NoValue) : UProps,
	                                                                          SwingNativeComponent.PropsProvider<Props<E>>,
	                                                                          PropsProvider<Props<E>, E>
	{
		override val comboBoxProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
				copy(swingProps = swingProps.builder())

		override fun withComboBoxProps(builder: Builder<Props<E>>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S, E>, E> : UProps
	{
		val comboBoxProps: Props<E>

		fun withComboBoxProps(builder: Builder<Props<E>>): S
	}

	private val itemListener = ItemListener { if(it.stateChange == ItemEvent.SELECTED) onSelect(it.item as E) }
	private val popupListener = PopupListenerDelegate({ props.onPopupShow.value },
	                                                  { props.onPopupHide.value },
	                                                  { props.onPopupCancel.value })

	private val model = MutableComboBoxModel(props.items.value ?: emptyList())

	private var renderer: CustomListCellRenderer<E>? = null
	private var editor: CustomComboBoxEditor<E>? = null

	override fun onCreate()
	{
		nativeComponent.addItemListener(itemListener)
		nativeComponent.addPopupMenuListener(popupListener)
		nativeComponent.model = model
	}

	override fun onDestroy()
	{
		nativeComponent.removeItemListener(itemListener)
		nativeComponent.removePopupMenuListener(popupListener)
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { nativeComponent }, props = props.swingProps)

	override fun onUpdate(previousProps: Props<E>?)
	{
		props.items.ifPresent { model.items = it }
		props.selectedItem.ifPresent { model.selectedItem = it }
		props.renderer.ifPresent { nativeComponent.renderer = getRenderer(it) }
		props.editor.ifPresent { nativeComponent.editor = getEditor(it) }
		props.editable.ifPresent { nativeComponent.isEditable = it }
		props.popupVisible.ifPresent { nativeComponent.isPopupVisible = it }
		props.lightweightPopup.ifPresent { nativeComponent.isLightWeightPopupEnabled = it }
		props.maximumRowCount.ifPresent { nativeComponent.maximumRowCount = it }
		props.prototypeDisplayValue.ifPresent { nativeComponent.prototypeDisplayValue = it }
	}

	private fun getRenderer(renderFunction: SwingRenderScope.(CustomListCellRenderer.Props<E>) -> SwingElement<*>) =
			renderer?.also { it.renderFunction = renderFunction } ?: CustomListCellRenderer(renderFunction).also { renderer = it }

	private fun getEditor(renderFunction: SwingRenderScope.(CustomComboBoxEditor.Props<E>) -> SwingElement<*>) =
			editor?.also { it.renderFunction = renderFunction } ?: CustomComboBoxEditor(renderFunction).also { editor = it }

	private fun onSelect(value: E)
	{
		props.onSelect.value?.invoke(value)
		invalidate()
	}
}

fun <E> SwingRenderScope.comboBox(key: Any = AutoKey) = comboBox(props = SwingComboBox.Props<E>(key))

internal fun <E> SwingRenderScope.comboBox(nativeComponent: () -> JComboBox<E> = ::JComboBox,
                                           props: SwingComboBox.Props<E>) =
		component({ SwingComboBox(nativeComponent(), it) }, props)

private typealias SCBProvider<P, E> = SwingComboBox.PropsProvider<P, E>
fun <P : SCBProvider<P, E>, E> SwingElement<P>.withComboBoxProps(builder: Builder<SwingComboBox.Props<E>>) =
		withProps { withComboBoxProps(builder) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.items(items: List<E>) =
		withComboBoxProps { copy(items = items.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.selectedItem(item: E?) =
		withComboBoxProps { copy(selectedItem = item.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.renderer(renderer: SwingRenderScope.(CustomListCellRenderer.Props<E>) -> SwingElement<*>) =
		withComboBoxProps { copy(renderer = renderer.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.editor(editor: SwingRenderScope.(CustomComboBoxEditor.Props<E>) -> SwingElement<*>) =
		withComboBoxProps { copy(editor = editor.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.editable(editable: Boolean) =
		withComboBoxProps { copy(editable = editable.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.popupVisible(visible: Boolean) =
		withComboBoxProps { copy(popupVisible = visible.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.lightweightPopup(popup: Boolean) =
		withComboBoxProps { copy(lightweightPopup = popup.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.maximumRowCount(count: Int) =
		withComboBoxProps { copy(maximumRowCount = count.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.prototypeDisplayValue(value: E?) =
		withComboBoxProps { copy(prototypeDisplayValue = value.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.onSelect(onSelect: (E) -> Unit) =
		withComboBoxProps { copy(onSelect = onSelect.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.onPopupShow(onPopupShow: () -> Unit) =
		withComboBoxProps { copy(onPopupShow = onPopupShow.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.onPopupHide(onPopupHide: () -> Unit) =
		withComboBoxProps { copy(onPopupHide = onPopupHide.prop()) }
fun <P : SCBProvider<P, E>, E> SwingElement<P>.onPopupCancel(onPopupCancel: () -> Unit) =
		withComboBoxProps { copy(onPopupCancel = onPopupCancel.prop()) }
