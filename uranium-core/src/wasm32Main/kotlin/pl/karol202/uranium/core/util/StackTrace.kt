package pl.karol202.uranium.core.util

internal actual class StackTrace(val stackElements: List<String>)
{
	actual companion object
	{
		actual val current get() = StackTrace(Exception().getStackTrace().toList())
	}
}
