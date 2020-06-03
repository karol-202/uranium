package pl.karol202.uranium.core.util

expect class StackTrace
{
	companion object
	{
		val current: StackTrace
	}
}
