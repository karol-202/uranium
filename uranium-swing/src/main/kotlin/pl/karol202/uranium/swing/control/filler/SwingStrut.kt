package pl.karol202.uranium.swing.control.filler

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.native.nativeComponent
import pl.karol202.uranium.swing.util.*
import pl.karol202.uranium.swing.util.Builder

class SwingStrut(initialProps: Props) : SwingAbstractAppComponent<SwingStrut.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val axis: StrutAxis,
	                 val width: Int) : UProps,
	                                   SwingNativeComponent.PropsProvider<Props>
	{
		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { createNativeComponent() }, props = props.swingProps)

	private fun createNativeComponent() = props.axis.createStrut(props.width)
}

fun SwingRenderScope.strut(key: Any = AutoKey,
                           axis: StrutAxis,
                           width: Int,
                           props: SwingStrut.Props = SwingStrut.Props(key = key, axis = axis, width = width)) =
		component(::SwingStrut, props)
