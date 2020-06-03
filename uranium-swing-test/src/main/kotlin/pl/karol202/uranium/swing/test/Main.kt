package pl.karol202.uranium.swing.test

import pl.karol202.uranium.core.common.BasicProps
import pl.karol202.uranium.core.common.UState
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.swing.control.button.*
import pl.karol202.uranium.swing.control.combobox.*
import pl.karol202.uranium.swing.control.label.label
import pl.karol202.uranium.swing.control.label.text
import pl.karol202.uranium.swing.control.list.*
import pl.karol202.uranium.swing.control.progress.maximum
import pl.karol202.uranium.swing.control.progress.minimum
import pl.karol202.uranium.swing.control.progress.progressBar
import pl.karol202.uranium.swing.control.progress.value
import pl.karol202.uranium.swing.control.scrollbar.ScrollBarAxis
import pl.karol202.uranium.swing.control.scrollbar.axis
import pl.karol202.uranium.swing.control.scrollbar.scrollBar
import pl.karol202.uranium.swing.control.scrollbar.value
import pl.karol202.uranium.swing.control.scrollpane.*
import pl.karol202.uranium.swing.control.slider.*
import pl.karol202.uranium.swing.control.text.*
import pl.karol202.uranium.swing.frame.SwingFrame
import pl.karol202.uranium.swing.layout.grid.contentColumns
import pl.karol202.uranium.swing.layout.grid.gridLayout
import pl.karol202.uranium.swing.layout.gridbag.cell
import pl.karol202.uranium.swing.layout.gridbag.gridBagLayout
import pl.karol202.uranium.swing.layout.gridbag.insets
import pl.karol202.uranium.swing.native.alignmentX
import pl.karol202.uranium.swing.util.*
import javax.swing.UIManager

fun main()
{
	UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
	SwingFrame.withRoot { counter(0) }.withTitle("Uranium test").withSize(640, 480).show()
}

class CounterComponent(props: BasicProps) : SwingStatefulComponent<BasicProps, CounterComponent.State>(props, State())
{
	data class State(val text: String = "start",
	                 val checked: Boolean = false,
	                 val items: List<String> = listOf("Kot", "Pies", "Koń"),
	                 val selectedItem: String? = null,
	                 val sliderValue: Int = 0,
	                 val selectedListItems: List<String> = listOf("Koń"),
	                 val textAreaText: String = "tekst") : UState

	override fun SwingRenderScope.render() = gridBagLayout {
		+ cell(x = if(state.checked) 1 else 0, y = 0) {
			label(key = 0).text("Tekst: ${state.text} Stan: ${state.checked} Slider: ${state.sliderValue}")
		}
		+ cell(x = if(state.checked) 0 else 1, y = 0, weightX = 1.0, fillX = true) {
			textField(key = 1).text(state.text).onTextChange { setText(it) }.columns(50)
		}
		+ cell(x = 2, y = 0) {
			checkBox(key = 2).text("Checkbox").selected(state.checked).onSelect { setChecked(it) }
		}
		+ cell(x = 3, y = 0) {
			radioButton(key = 3).text("Radio").selected(state.checked).onSelect { setChecked(it) }
		}
		+ cell(x = 4, y = 0) {
			toggleButton(key = 4).text(state.text).selected(state.checked).onClick { setChecked(false) }
		}
		+ cell(x = 4, y = 1, weightX = 1.0) {
			comboBox<String>(key = 5).items(state.items).renderer { props ->
				label().text(props.item?.let { "Zwierz: $it" } ?: "Brak")
			}.editable(true).editor { props ->
				editorComponent(initialValue = props.item, onApply = {
					addItem(it)
					props.onEdit(it)
					setSelectedItem(it)
				})
			}.selectedItem(state.selectedItem).onSelect { setSelectedItem(it) }
		}
		+ cell(x = 0, y = 1, fillX = true) {
			slider(key = 6).minimum(50).maximum(100).value(state.sliderValue).onChange { setSliderValue(it) }
		}
		+ cell(x = 2, y = 1, fillX = true, insets = insets(8, 8, 8, 8)) {
			list<String>(key = 7).items(state.items).selectionMode(ListSelectionMode.MULTIPLE_INTERNAL_SELECTION)
					.selectedItems(state.selectedListItems).onSelect { setSelectedListItems(it) }
					.renderer {
						val prefix = if(it.selected) "> " else ""
						label().text("$prefix${it.item}").alignmentX(1f)
					}
		}
		+ cell(x = 3, y = 1, fillX = true) {
			progressBar(key = 8).minimum(0).maximum(200).value((state.sliderValue - 50) * 4)
		}
		+ cell(x = 0, y = 2, width = 2, fillX = true, fillY = true) {
			scrollPaneUnidirectional(key = 9).content {
				textArea().columns(100).rows(10).text(state.textAreaText).onTextChange { setTextAreaText(it) }
			}.lowerRightCorner { button() }
		}
		+ cell(x = 2, y = 2, fillY = true, paddingY = 100) {
			scrollBar(key = 10).axis(ScrollBarAxis.HORIZONTAL).value(state.sliderValue)
		}
	}

	private fun setText(text: String) = setState { copy(text = text) }

	private fun setChecked(checked: Boolean) = setState { copy(checked = checked) }

	private fun addItem(item: String) = setState { copy(items = items + item) }

	private fun setSelectedItem(item: String) = setState { copy(selectedItem = item) }

	private fun setSliderValue(value: Int) = setState { copy(sliderValue = value) }

	private fun setSelectedListItems(items: List<String>) = setState { copy(selectedListItems = items) }

	private fun setTextAreaText(text: String) = setState { copy(textAreaText = text) }
}

fun SwingRenderScope.counter(key: Any) = component(::CounterComponent, BasicProps(key))
