package pl.karol202.uranium.core.manager

interface RenderScheduler
{
	fun submit(function: () -> Unit)
}
