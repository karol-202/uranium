package pl.karol202.uranium.core.component

/**
 * Context a component is used in, kept in order for the component to be able to inform
 * about changes such as need of invalidation.
 */
interface ComponentContext
{
	/**
	 * Notifies that component should be invalidated, for example in case of state change.
	 * Should not be used directly by end user.
	 */
	fun invalidate()
}

internal fun componentContext(invalidate: () -> Unit) = object : ComponentContext
{
	override fun invalidate() = invalidate()
}
