package pl.karol202.uranium.core.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import pl.karol202.uranium.core.util.StackTrace

private const val MAX_BUFFER_SIZE = 64

fun queueRenderScheduler(queueSize: Int = MAX_BUFFER_SIZE): RenderScheduler =
		QueueRenderScheduler(CoroutineScope(Dispatchers.Main), queueSize)

internal class QueueRenderScheduler(coroutineScope: CoroutineScope,
                                    queueSize: Int) : RenderScheduler
{
	internal data class RenderRequest(val execute: () -> Unit,
	                                  val stackTrace: StackTrace) // For debugging purposes

	private val scheduleChannel = Channel<RenderRequest>(queueSize)

	init
	{
		coroutineScope.launch { run() }
	}

	private suspend fun run()
	{
		for(request in scheduleChannel) request.execute()
	}

	override fun submit(function: () -> Unit)
	{
		val request = RenderRequest(function, StackTrace.current)
		scheduleChannel.offer(request) || throw RuntimeException("Too many scheduled renders (probably an infinite loop)")
	}
}
