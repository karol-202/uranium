package pl.karol202.uranium.core.manager

import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.native.UNativeContainer
import pl.karol202.uranium.core.native.asNode
import pl.karol202.uranium.core.tree.TreeNode
import pl.karol202.uranium.core.tree.createNode

/**
 * Class for managing the rendering and committing process of root component
 * (as all the children components manage themselves at their own).
 *
 * @param N native marker
 * @param P props type
 * @param element element used to create root component
 * @param container native container that will contain natives generated (in committing phase) from the root component
 * @param scheduler [RenderScheduler] used to schedule render requests
 */
class RenderManager<N, P : UProps>(element: UElement<N, P>,
                                   container: UNativeContainer<N>,
                                   private val scheduler: RenderScheduler)
{
	private val rootTreeNode = element.createNode { scheduleInvalidate(it) }

	private var rootNativeNode = container.asNode()

	/**
	 * Schedules a init request to be executed by scheduler.
	 *
	 * This is preferred way of starting uranium application that should
	 * be called by platform implementations.
	 */
	fun scheduleInit() = scheduler.submit { init() }

	/**
	 * Initializes the root component (and all the children recursively),
	 * then commits changes to the natives hierarchy.
	 *
	 * Should be called exactly once.
	 */
	fun init()
	{
		rootTreeNode.init()
		commit()
	}

	/**
	 * Schedules a props change request to be executed by scheduler.
	 *
	 * @param props new props
	 */
	fun scheduleReuse(props: P) = scheduler.submit { reuse(props) }

	/**
	 * Provides new props for the root component (leading to possible rerender),
	 * then commits changes to the natives hierarchy.
	 *
	 * @param props new props
	 */
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
