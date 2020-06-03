package pl.karol202.uranium.core.tree

import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement

internal sealed class TreeNodeOperation<N>
{
	class CreateAndAttachNode<N>(val element: UElement<N, *>,
	                             val index: Int) : TreeNodeOperation<N>()

	class UpdateNode<N, P : UProps>(val node: TreeNode<N, P>,
	                                val props: P) : TreeNodeOperation<N>()

	class DestroyAndDetachNode<N>(val node: TreeNode<N, *>) : TreeNodeOperation<N>()

	class AttachNode<N>(val node: TreeNode<N, *>,
	                    val index: Int) : TreeNodeOperation<N>()

	class DetachNode<N>(val node: TreeNode<N, *>) : TreeNodeOperation<N>()
}
