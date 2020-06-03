package pl.karol202.uranium.swing.control.button

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.util.*
import javax.swing.JButton

class SwingButton(private val nativeComponent: JButton,
                  initialProps: SwingAbstractButton.Props) : SwingAbstractAppComponent<SwingAbstractButton.Props>(initialProps)
{
	override fun URenderScope<Swing>.render() = abstractButton(nativeComponent = { nativeComponent }, props = props)
}

fun SwingRenderScope.button(key: Any = AutoKey) = button(props = SwingAbstractButton.Props(key))

internal fun SwingRenderScope.button(nativeComponent: () -> JButton = ::JButton,
                                     props: SwingAbstractButton.Props) =
		component({ SwingButton(nativeComponent(), it) }, props)
