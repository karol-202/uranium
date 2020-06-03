package pl.karol202.uranium.swing.util

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.render

/*
Component useful for being root in component hierarchies,
because RenderManager doesn't allow to change its root component type after its instantiation
and only way to change it is to wrap it with some other component (SwingSingleWrapper) that
will stay the same all the time but will allow to freely change its contents.
*/
class SwingSingleWrapper(props: Props) : SwingAbstractAppComponent<SwingSingleWrapper.Props>(props)
{
	data class Props(override val key: Any = AutoKey,
	                 val child: SwingElement<*>) : UProps

	override fun SwingRenderScope.render() = props.child
}

fun SwingRenderScope.singleWrapper(key: Any = AutoKey,
                                   block: SwingRenderScope.() -> SwingElement<*>) =
		component(::SwingSingleWrapper, SwingSingleWrapper.Props(key = key, child = block.render()))
