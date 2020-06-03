package pl.karol202.uranium.core.util

actual class StackTrace(val stackElements: List<StackTraceElement>)
{
	actual companion object
	{
		actual val current get() = StackTrace(Exception().stackTrace.toList())
	}
}
