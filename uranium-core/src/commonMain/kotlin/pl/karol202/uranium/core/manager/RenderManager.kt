package pl.karol202.uranium.core.manager

import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.native.UNativeContainer
import pl.karol202.uranium.core.native.asNode
import pl.karol202.uranium.core.tree.TreeNode
import pl.karol202.uranium.core.tree.createNode

class RenderManager<N, P : UProps>(element: UElement<N, P>,
                                   container: UNativeContainer<N>,
                                   private val scheduler: RenderScheduler)
{
	private val rootTreeNode = element.createNode { scheduleInvalidate(it) }

	private var rootNativeNode = container.asNode()

	fun scheduleInit() = scheduler.submit { init() }

	fun init()
	{
		rootTreeNode.init()
		commit()
	}

	fun scheduleReuse(props: P) = scheduler.submit { reuse(props) }

	fun reuse(props: P)
	{
		rootTreeNode.reuse(props)
		commit()
	}

	private fun scheduleInvalidate(node: TreeNode<N, *>) = scheduler.submit { invalidate(node) }

	private fun invalidate(node: TreeNode<N, *>)
	{
		node.invalidate()
		commit()
	}

	private fun commit() = rootNativeNode.transformedTo(rootTreeNode.nativeNodes).also { rootNativeNode = it }
}
