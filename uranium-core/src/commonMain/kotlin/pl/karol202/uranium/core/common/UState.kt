package pl.karol202.uranium.core.common

import pl.karol202.uranium.core.component.UAbstractComponent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface UState

interface UStateful<S : UState>
{
	var state: S
}

fun <S : UState> UStateful<S>.setState(builder: S.() -> S)
{
	state = state.builder()
}

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
