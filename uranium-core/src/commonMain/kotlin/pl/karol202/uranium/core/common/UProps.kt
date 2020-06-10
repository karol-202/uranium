package pl.karol202.uranium.core.common

/**
 * Props are input data to components.
 * [UProps] serves as a base interface for all props classes.
 * All props classes must implement this interface.
 *
 * All props classes must contain key, that will be used for created component.
 * This is enforced by subclassing [UKeyProvider] by [UProps], what forces all classes implementing
 * [UProps] to override key property.
 *
 * All props classes are required to be immutable, because the dispatching algorithm
 * determines whether props have changed comparing new props with old props based on instance,
 * so if the new props are the same object as old props they will be always considered identical
 * leading to issues when invalidating.
 *
 * All props classes should properly implement [Any.equals] and [Any.hashCode] methods,
 * because props potentially can be also compared using these methods (even if at a given moment
 * [Any.equals] and [Any.hashCode] aren't used, it doesn't mean that they will not be used in future as
 * internals of component dispatcher are subject to change and it shouldn't be cause of any code breaks).
 *
 * Given that, it is suggested to make all the props classes immutable data classes.
 */
interface UProps : UKeyProvider

/**
 * Basic props implementation for simplest cases, where only a key is needed.
 *
 * @param key component's key
 */
data class BasicProps(override val key: Any) : UProps


/**
 * Interface for all classes that contains props (component, element, etc.)
 *
 * @param P props type
 */
interface UPropsProvider<P : UProps> : UKeyProvider
{
	/**
	 * Provided props
	 */
	val props: P

	/**
	 * Key provided by the props
	 */
	override val key get() = props.key
}
