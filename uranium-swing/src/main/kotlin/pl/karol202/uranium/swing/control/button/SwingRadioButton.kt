package pl.karol202.uranium.swing.control.button

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.swing.util.SwingRenderScope
import javax.swing.JRadioButton

fun SwingRenderScope.radioButton(key: Any = AutoKey) = radioButton(props = SwingToggleButton.Props(key))

internal fun SwingRenderScope.radioButton(nativeComponent: () -> JRadioButton = ::JRadioButton,
                                          props: SwingToggleButton.Props) =
		toggleButton(nativeComponent, props)
