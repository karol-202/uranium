package pl.karol202.uranium.core.native

import pl.karol202.uranium.core.util.elementInserted
import pl.karol202.uranium.core.util.elementRemoved
import pl.karol202.uranium.core.util.elementReplaced

internal class UNativeNode<N>(private val native: UNative<N>,
                              private val children: List<UNativeNode<N>> = emptyList())
{
	private val nativeAsContainer get() = native as UNativeContainer

	fun transformedTo(targetChildren: List<UNativeNode<N>>) =
			UNativeNode(native, children.childrenTransformedTo(targetChildren).detachExcessiveNodes(targetChildren.size))

	private fun List<UNativeNode<N>>.childrenTransformedTo(targetChildren: List<UNativeNode<N>>) =
			targetChildren.foldIndexed(this) { targetIndex, currentChildren, targetNode ->
				currentChildren.childTransformedTo(targetNode, targetIndex)
			}

	private fun List<UNativeNode<N>>.childTransformedTo(targetNode: UNativeNode<N>, targetIndex: Int): List<UNativeNode<N>>
	{
		val old = withIndex().find { it.value.native == targetNode.native }
		val transformedNode = (old?.value ?: UNativeNode(targetNode.native)).transformedTo(targetNode.children)
		return when
		{
			old == null -> nodeAttached(transformedNode, targetIndex)
			old.index > targetIndex -> nodeReattached(old.value, transformedNode, targetIndex)
			old.index < targetIndex -> throw IllegalStateException("Diffing algorithm bug!")
			else -> elementReplaced(old.value, transformedNode)
		}
	}

	private fun List<UNativeNode<N>>.detachExcessiveNodes(limit: Int) =
			mapIndexedNotNull { index, node ->
				if(index < limit) node
				else null.also { detach(node) }
			}

	private fun List<UNativeNode<N>>.nodeAttached(node: UNativeNode<N>, targetIndex: Int) =
			elementInserted(node, targetIndex).also { attach(node, targetIndex) }

	private fun List<UNativeNode<N>>.nodeDetached(node: UNativeNode<N>) =
			elementRemoved(node).also { detach(node) }

	private fun List<UNativeNode<N>>.nodeReattached(oldNode: UNativeNode<N>, targetNode: UNativeNode<N>, targetIndex: Int) =
			nodeDetached(oldNode).nodeAttached(targetNode, targetIndex)

	private fun attach(node: UNativeNode<N>, targetIndex: Int) = nativeAsContainer.attach(node.native, targetIndex)

	private fun detach(node: UNativeNode<N>) = nativeAsContainer.detach(node.native)
}
