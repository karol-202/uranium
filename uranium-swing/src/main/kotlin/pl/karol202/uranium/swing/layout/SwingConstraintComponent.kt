package pl.karol202.uranium.swing.layout

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.native.SwingNative
import pl.karol202.uranium.swing.util.Swing
import pl.karol202.uranium.swing.util.SwingAbstractNativeComponent
import pl.karol202.uranium.swing.util.SwingElement
import pl.karol202.uranium.swing.util.SwingRenderScope

class SwingConstraintComponent(constraint: Any,
                               initialProps: Props) : SwingAbstractNativeComponent<SwingConstraintComponent.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
	                 val content: SwingElement<*>) : UProps

	override val native = SwingNative.constraint(constraint)

	override fun URenderScope<Swing>.render() = listOf(props.content)
}

internal fun SwingRenderScope.constraint(constraint: Any,
                                         content: SwingElement<*>,
                                         key: Any = content.key) =
		constraint(constraint, SwingConstraintComponent.Props(key, content))

internal fun SwingRenderScope.constraint(constraint: Any,
                                         props: SwingConstraintComponent.Props) =
		component({ SwingConstraintComponent(constraint, it) }, props)
