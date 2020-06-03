package pl.karol202.uranium.swing.control.filler

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import pl.karol202.uranium.swing.util.Builder
import java.awt.Dimension
import javax.swing.Box
import javax.swing.JComponent

class SwingRigidArea(initialProps: Props) : SwingAbstractAppComponent<SwingRigidArea.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val size: Dimension) : UProps,
	                                        SwingNativeComponent.PropsProvider<Props>
	{
		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { createNativeComponent() }, props = props.swingProps)

	private fun createNativeComponent() = Box.createRigidArea(props.size) as JComponent
}

fun SwingRenderScope.rigidArea(key: Any = AutoKey,
                               size: Dimension,
                               props: SwingRigidArea.Props = SwingRigidArea.Props(key = key, size = size)) =
		component(::SwingRigidArea, props)
