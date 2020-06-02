package pl.karol202.uranium.core.util

internal expect class StackTrace
{
	companion object
	{
		val current: StackTrace
	}
}
