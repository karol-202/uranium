package pl.karol202.uranium.core.manager

fun instantRenderScheduler(): RenderScheduler = InstantRenderScheduler()

internal class InstantRenderScheduler : RenderScheduler
{
	override fun submit(function: () -> Unit) = function()
}
