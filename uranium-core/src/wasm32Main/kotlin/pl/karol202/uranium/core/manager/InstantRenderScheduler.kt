package pl.karol202.uranium.core.manager

/**
 * Creates a render scheduler that immediately executes all requests.
 * Queue render scheduler could not be easily implemented because of lack of coroutines support for WASM.
 */
fun instantRenderScheduler(): RenderScheduler = InstantRenderScheduler()

internal class InstantRenderScheduler : RenderScheduler
{
	override fun submit(function: () -> Unit) = function()
}
