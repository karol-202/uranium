package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UProps

/**
 * Base implementation of [UComponent], that every component should implement directly or indirectly.
 *
 * Generally, inheritance of components should be avoided and composition should be used instead.
 * One of the rare cases where components inheritance is allowed are basic platform components.
 * Implementations of uranium for different platforms may differ for example in how many children
 * components may a component have (one or unlimited) and which natives are allowed. So every
 * platform implementation may define one or more base classes inheriting from [UAbstractComponent],
 * that every component using the implementation should extend.
 *
 * @param N native marker
 * @param P props type
 * @param props initial props of the component
 */
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

	/**
	 * Lifecycle method called after instantiation of a component.
	 */
	protected open fun onCreate() { }

	/**
	 * Lifecycle method called when a component is no longer used and is being detached from component hierarchy
	 */
	protected open fun onDestroy() { }

	override fun needsUpdate(newProps: P) = true

	final override fun modifyPropsInternal(props: P)
	{
		this.props = props
	}

	/**
	 * Notifies that the component should be invalidated.
	 * Usually there is no need to call it directly.
	 */
	fun invalidate() = requireContext().invalidate()

	private fun requireContext() = context ?: throw IllegalStateException("Not attached")
}
