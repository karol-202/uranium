package pl.karol202.uranium.core.tree

import pl.karol202.uranium.core.common.UKeyProvider
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.component.UComponent
import pl.karol202.uranium.core.component.UNativeComponent
import pl.karol202.uranium.core.component.componentContext
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.native.UNativeNode
import pl.karol202.uranium.core.tree.TreeNodeOperation.*
import pl.karol202.uranium.core.util.*
import pl.karol202.uranium.core.util.NativeList
import pl.karol202.uranium.core.util.inserted
import pl.karol202.uranium.core.util.emptyNativeList
import pl.karol202.uranium.core.util.nativeListOf
import kotlin.collections.fold
import kotlin.reflect.KClass

internal fun <N, P : UProps> UElement<N, P>.createNode(invalidateCallback: (TreeNode<N, *>) -> Unit) =
		TreeNode(createComponent(), propsClass, invalidateCallback)

internal class TreeNode<N, P : UProps> internal constructor(private val component: UComponent<N, P>,
                                                            internal val propsClass: KClass<P>,
                                                            private val invalidateCallback: (TreeNode<N, *>) -> Unit) : UKeyProvider
{
	override val key get() = component.key
	internal val props get() = component.props

	private var children = emptyNativeList<TreeNode<N, *>>()

	val nativeNodes: NativeList<UNativeNode<N>>
		get() = when(component)
		{
			is UNativeComponent<N, P> -> nativeListOf(UNativeNode(component.native, children.flatMap { it.nativeNodes }))
			else -> children.flatMap { it.nativeNodes }
		}

	fun init()
	{
		createComponent()
		render()
		update()
	}

	private fun createComponent() = component.create(componentContext { invalidateCallback(this) })

	fun invalidate()
	{
		render()
		update(component.props)
	}

	private fun render()
	{
		children = dispatchDiff(children, component.render().toNativeList()).fold(children) { children, op ->
			op.applyTo(children)
		}
	}

	private fun TreeNodeOperation<N>.applyTo(children: NativeList<TreeNode<N, *>>) = when(this)
	{
		is CreateAndAttachNode<N> -> children.inserted(createChild(element), index)
		is UpdateNode<N, *> -> children.also { this.apply() }
		is DestroyAndDetachNode<N> -> children - node.also { it.destroy() }
		is AttachNode<N> -> children.inserted(node, index)
		is DetachNode<N> -> children - node
	}

	private fun createChild(element: UElement<N, *>) = element.createNode(invalidateCallback).also { it.init() }

	private fun <P : UProps> UpdateNode<N, P>.apply() = node.reuse(props)

	fun reuse(props: P)
	{
		if(needsUpdate(props)) keepProps { prevProps ->
			setProps(props)
			render()
			update(prevProps)
		}
		else setProps(props)
	}

	// Returning false doesn't have to necessarily mean that props haven't changed
	private fun needsUpdate(props: P) = component.props != props

	private fun <R> keepProps(block: (P) -> R) = component.props.let(block)

	private fun setProps(props: P) = component.modifyPropsInternal(props)

	private fun update(previousProps: P? = null) = component.onUpdate(previousProps)

	private fun destroy()
	{
		destroyChildren()
		destroyComponent()
	}

	private fun destroyChildren()
	{
		children.forEach { it.destroy() }
		children = emptyNativeList()
	}

	private fun destroyComponent() = component.destroy()
}
