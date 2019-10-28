package pl.karol202.uranium.swing.control.button

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.swing.util.SwingRenderScope
import javax.swing.JRadioButton

fun SwingRenderScope.radioButton(nativeComponent: () -> JRadioButton = ::JRadioButton,
                                 key: Any = AutoKey,
                                 props: SwingToggleButton.Props = SwingToggleButton.Props(key)) =
		toggleButton(nativeComponent, key, props)
