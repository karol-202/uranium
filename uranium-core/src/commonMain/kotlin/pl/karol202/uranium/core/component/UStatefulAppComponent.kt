package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.common.UState
import kotlin.jvm.JvmName

abstract class UStatefulAppComponent<N, P : UProps, S : UState>(props: P,
                                                                state: S) : UAbstractAppComponent<N, P>(props)
{
	@set:JvmName("setStateProperty")
	var state = state
		set(value)
		{
			if(value == field) return
			field = value
			invalidate()
		}

	protected fun setState(builder: S.() -> S)
	{
		this.state = state.builder()
	}
}
