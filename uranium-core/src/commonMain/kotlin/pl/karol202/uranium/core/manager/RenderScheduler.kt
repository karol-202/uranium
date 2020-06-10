package pl.karol202.uranium.core.manager

/**
 * [RenderScheduler] schedules rendering request for future or instant execution.
 *
 * For example a queue can be used to implement it.
 */
interface RenderScheduler
{
	/**
	 * Submits a request.
	 *
	 * @param function function that will be executed in future or now
	 */
	fun submit(function: () -> Unit)
}
