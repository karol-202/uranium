package pl.karol202.uranium.core.element

import pl.karol202.uranium.core.common.UPropsProvider
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.component.UComponent
import pl.karol202.uranium.core.render.URenderScope
import kotlin.reflect.KClass

/**
 * [UElement] objects are lightweight information about component that will be created.
 * Elements are returned by [UComponent] render methods to construct component hierarchy.
 *
 * @param N native marker
 * @param P props type
 * @param constructor function returning a new component with given props
 * @param props props that will be used to construct the component
 * @param propsClass props type, used internally for component dispatching
 */
class UElement<N, P : UProps>(private val constructor: (P) -> UComponent<N, P>,
                              override val props: P,
                              internal val propsClass: KClass<P>) : UPropsProvider<P>
{
	/**
	 * Returns new element with props returned by builder function
	 *
	 * @param propsBuilder function having old props as receiver and returning new props
	 * @return new element with changed props
	 */
	fun withProps(propsBuilder: P.() -> P) = UElement(constructor, props.propsBuilder(), propsClass)

	internal fun createComponent() = constructor(props)
}

/**
 * Function returning element from given constructor and props.
 * Can be used to implement component-creating functions.
 * Component-creating functions can be created for all components to simplify the process of
 * creating components hierarchy.
 *
 * For example a following function:
 *     fun SampleRenderScope.button(key: Any = AutoKey, text: String = "Default") =
 *         component(::SampleButton, SampleButton.Props(key, text))
 * can be used in render function:
 *     + button(text = "My text")
 * instead of:
 *     + UElement(::SampleButton, SampleButton.Props(AutoKey, "My text"), SampleButton.Props::class)
 *
 * @param N native marker
 * @param P props type
 * @param constructor function returning a new component with given props
 * @param props props that will be used to construct the component
 * @return created [UElement]
 */
inline fun <N, reified P : UProps> URenderScope<N>.component(noinline constructor: (P) -> UComponent<N, P>, props: P) =
		UElement(constructor, props, P::class)
