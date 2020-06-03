package pl.karol202.uranium.swing.control.slider

import pl.karol202.uranium.swing.util.toHashtable
import java.awt.Color
import java.util.*
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JSlider
import javax.swing.plaf.UIResource

interface SliderLabelTable
{
	companion object
	{
		fun standard(increment: Int, start: Int? = null) =
				StandardSliderLabelTable(increment, start)

		fun valueMap(vararg values: Int) = valueMap(*values.map { it to it.toString() }.toTypedArray())

		fun valueMap(vararg pairs: Pair<Int, String>) = ValueMapSliderLabelTable(pairs.toMap())
	}

	fun createLabelTable(slider: JSlider): Dictionary<Int, JComponent>
}

class StandardSliderLabelTable(private val increment: Int,
                               private val start: Int?) : SliderLabelTable
{
	@Suppress("UNCHECKED_CAST")
	override fun createLabelTable(slider: JSlider) =
			slider.createStandardLabels(increment, start ?: slider.minimum) as Dictionary<Int, JComponent>
}

class ValueMapSliderLabelTable(private val valueMap: Map<Int, String>) : SliderLabelTable
{
	private class Label(private val slider: JSlider?,
	                    text: String) : JLabel(text, CENTER)
	{
		override fun getFont() = super.getFont()?.takeIf { it !is UIResource } ?: slider?.font

		override fun getForeground(): Color? =
				super.getForeground()?.takeIf { it !is UIResource } ?:
				slider?.foreground?.takeIf { it !is UIResource } ?:
				super.getForeground()
	}

	override fun createLabelTable(slider: JSlider) = valueMap.mapValues { createLabel(slider, it.value) }.toHashtable()

	private fun createLabel(slider: JSlider, text: String): JComponent = Label(slider, text)
}
