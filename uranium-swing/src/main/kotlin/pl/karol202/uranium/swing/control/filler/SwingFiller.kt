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

class SwingFiller(initialProps: Props) : SwingAbstractAppComponent<SwingFiller.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val minimumSize: Dimension,
	                 val preferredSize: Dimension,
	                 val maximumSize: Dimension) : UProps,
	                                               SwingNativeComponent.PropsProvider<Props>
	{
		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())
	}

	override fun URenderScope<Swing>.render() =
			nativeComponent(nativeComponent = { createNativeComponent() }, props = props.swingProps)


	private fun createNativeComponent() = Box.Filler(props.minimumSize, props.preferredSize, props.maximumSize)
}

fun SwingRenderScope.filler(key: Any = AutoKey,
                            minimumSize: Dimension,
                            preferredSize: Dimension,
                            maximumSize: Dimension,
                            props: SwingFiller.Props = SwingFiller.Props(key = key,
                                                                         minimumSize = minimumSize,
                                                                         preferredSize = preferredSize,
                                                                         maximumSize = maximumSize)) =
		component(::SwingFiller, props)
