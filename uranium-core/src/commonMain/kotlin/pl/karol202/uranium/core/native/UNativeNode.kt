package pl.karol202.uranium.core.native

import pl.karol202.uranium.core.util.*
import pl.karol202.uranium.core.util.NativeList
import pl.karol202.uranium.core.util.removed

internal class UNativeNode<N>(private val native: UNative<N>,
                              private val children: NativeList<UNativeNode<N>> = emptyNativeList())
{
	private val nativeAsContainer get() = native as UNativeContainer

	fun transformedTo(targetChildren: NativeList<UNativeNode<N>>) =
			UNativeNode(native, children.childrenTransformedTo(targetChildren).detachExcessiveNodes(targetChildren.size))

	private fun NativeList<UNativeNode<N>>.childrenTransformedTo(targetChildren: NativeList<UNativeNode<N>>) =
			targetChildren.foldIndexed(this) { targetIndex, currentChildren, targetNode ->
				currentChildren.childTransformedTo(targetNode, targetIndex)
			}

	private fun NativeList<UNativeNode<N>>.childTransformedTo(targetNode: UNativeNode<N>, targetIndex: Int): NativeList<UNativeNode<N>>
	{
		val old = withIndex().find { it.value.native === targetNode.native }
		val transformedNode = (old?.value ?: UNativeNode(targetNode.native)).transformedTo(targetNode.children)
		return when
		{
			old == null -> nodeAttached(transformedNode, targetIndex)
			old.index > targetIndex -> nodeReattached(old.value, transformedNode, targetIndex)
			old.index < targetIndex -> throw IllegalStateException("Diffing algorithm bug!")
			else -> replaced(transformedNode, targetIndex)
		}
	}

	private fun NativeList<UNativeNode<N>>.detachExcessiveNodes(limit: Int) =
			mapIndexedNotNull { index, node ->
				if(index < limit) node
				else null.also { detach(node) }
			}

	private fun NativeList<UNativeNode<N>>.nodeAttached(node: UNativeNode<N>, targetIndex: Int) =
			inserted(node, targetIndex).also { attach(node, targetIndex) }

	private fun NativeList<UNativeNode<N>>.nodeDetached(node: UNativeNode<N>) =
			removed(node).also { detach(node) }

	private fun NativeList<UNativeNode<N>>.nodeReattached(oldNode: UNativeNode<N>, targetNode: UNativeNode<N>, targetIndex: Int) =
			nodeDetached(oldNode).nodeAttached(targetNode, targetIndex)

	private fun attach(node: UNativeNode<N>, targetIndex: Int) = nativeAsContainer.attach(node.native, targetIndex)

	private fun detach(node: UNativeNode<N>) = nativeAsContainer.detach(node.native)
}
