package pl.karol202.uranium.core.common

import pl.karol202.uranium.core.component.UAbstractComponent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * [UState] serves as a base interface that all state classes should implement.
 *
 * State (as opposed to props) is an internal state of a component.
 * For a component to have state, it should implement [UStateful].
 */
interface UState

/**
 * Interface that a component should implement to have its own state.
 *
 * @param S concrete type of state class
 */
interface UStateful<S : UState>
{
	/**
	 * Current state of the component.
	 * Should be implemented by using delegating to value returned by [state].
	 */
	var state: S
}

/**
 * Allows to change component's state by using a builder method
 * with receiver being the initial state (before change)
 *
 * @param S type of changed state
 * @param builder builder method returning new state that will be assigned to component's state property
 * @receiver [UStateful] implementation, component in most cases
 */
fun <S : UState> UStateful<S>.setState(builder: S.() -> S)
{
	state = state.builder()
}

/**
 * Method returning delegate to be used to implement state property of [UStateful].
 * When using this delegate a component is automatically invalidated on state changes.
 *
 * @param S type of changed state
 * @param initialState state that a component will have until a change is performed
 * @receiver component, owner of the state
 * @return property delegate allowing for state storage
 */
fun <S : UState> UAbstractComponent<*, *>.state(initialState: S) = object : ReadWriteProperty<Any, S>
{
	private var state = initialState

	override fun getValue(thisRef: Any, property: KProperty<*>) = state

	override fun setValue(thisRef: Any, property: KProperty<*>, value: S)
	{
		if(value == state) return
		state = value
		invalidate()
	}
}
