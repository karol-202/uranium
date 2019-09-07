package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.context.InvalidateableContext

abstract class AbstractComponent<N, P : UProps>(props: P) : UComponent<N, P>
{
	final override var props = props
		private set

	protected var parentContext: InvalidateableContext<N>? = null
		private set

	override fun attach(parentContext: InvalidateableContext<N>)
	{
		this.parentContext = parentContext
		onAttach(parentContext)
	}

	override fun detach()
	{
		onDetach(parentContext ?: throw IllegalStateException("Already detached"))
		parentContext = null
	}

	protected open fun onAttach(parentContext: InvalidateableContext<N>) { }

	protected open fun onDetach(parentContext: InvalidateableContext<N>) { }

	override fun modifyPropsInternal(props: P)
	{
		this.props = props
	}
}