package pl.karol202.uranium.swing.control.filler

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import pl.karol202.uranium.swing.util.Builder

class SwingGlue(initialProps: Props) : SwingAbstractAppComponent<SwingGlue.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val axis: GlueAxis) : UProps,
	                                       SwingNativeComponent.PropsProvider<Props>
	{
		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { createNativeComponent() }, props = props.swingProps)

	private fun createNativeComponent() = props.axis.createGlue()
}

fun SwingRenderScope.glue(key: Any = AutoKey,
                          axis: GlueAxis,
                          props: SwingGlue.Props = SwingGlue.Props(key = key, axis = axis)) = component(::SwingGlue, props)
