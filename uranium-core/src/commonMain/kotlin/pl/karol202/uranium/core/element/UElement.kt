package pl.karol202.uranium.core.element

import pl.karol202.uranium.core.common.UPropsProvider
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.component.UComponent
import pl.karol202.uranium.core.render.URenderScope
import kotlin.reflect.KClass

class UElement<N, P : UProps>(private val constructor: (P) -> UComponent<N, P>,
                              override val props: P,
                              internal val propsClass: KClass<P>) : UPropsProvider<P>
{
	fun withProps(propsBuilder: P.() -> P) = UElement(constructor, props.propsBuilder(), propsClass)

	internal fun createComponent() = constructor(props)
}

inline fun <N, reified P : UProps> URenderScope<N>.component(noinline constructor: (P) -> UComponent<N, P>, props: P) =
		UElement(constructor, props, P::class)
