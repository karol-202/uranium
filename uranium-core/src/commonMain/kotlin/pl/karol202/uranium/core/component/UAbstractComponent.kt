package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UProps

abstract class UAbstractComponent<N, P : UProps>(props: P) : UComponent<N, P>
{
	final override var props = props
		private set

	private var context: ComponentContext? = null

	final override fun create(context: ComponentContext)
	{
		this.context = context
		onCreate()
	}

	final override fun destroy()
	{
		onDestroy()
		context = null
	}

	protected open fun onCreate() { }

	protected open fun onDestroy() { }

	final override fun modifyPropsInternal(props: P)
	{
		this.props = props
	}

	protected fun invalidate() = requireContext().invalidate()

	private fun requireContext() = context ?: throw IllegalStateException("Not attached")
}
