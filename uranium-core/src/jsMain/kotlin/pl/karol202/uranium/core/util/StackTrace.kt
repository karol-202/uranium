package pl.karol202.uranium.core.util

internal actual class StackTrace(val content: dynamic)
{
	actual companion object
	{
		actual val current get() = StackTrace(js("new Error().stack"))
	}
}
