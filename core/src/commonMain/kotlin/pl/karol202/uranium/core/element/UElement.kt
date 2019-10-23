package pl.karol202.uranium.core.element

import pl.karol202.uranium.core.common.PropsProvider
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.component.UComponent
import kotlin.reflect.KClass

class UElement<N, P : UProps>(private val constructor: (P) -> UComponent<N, P>,
                              override val props: P,
                              internal val propsClass: KClass<P>) : PropsProvider<P>
{
	fun withProps(propsBuilder: P.() -> P) = UElement(constructor, props.propsBuilder(), propsClass)

	internal fun createComponent() = constructor(props)
}
