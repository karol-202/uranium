package pl.karol202.uranium.core.schedule

internal interface RenderScheduler<N>
{
	fun submit(function: () -> Unit)
}
