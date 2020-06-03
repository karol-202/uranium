package pl.karol202.uranium.core.tree

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.KeyProvider
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.tree.TreeNodeOperation.CreateAndAttachNode
import pl.karol202.uranium.core.util.elementInserted
import pl.karol202.uranium.core.util.elementRemoved
import kotlin.reflect.KClass

private sealed class DispatchUnit<N, P : UProps>(val props: P,
                                                 val propsClass: KClass<P>): KeyProvider
{
	override val key get() = props.key
	val asExisting get() = this as Existing

	class Existing<N, P : UProps>(val node: TreeNode<N, P>) : DispatchUnit<N, P>(node.props, node.propsClass)

	class New<N, P : UProps>(val element: UElement<N, P>) : DispatchUnit<N, P>(element.props, element.propsClass)
}

private data class DispatchingState<N>(val currentUnits: List<DispatchUnit<N, *>>,
                                       val operations: List<TreeNodeOperation<N>>)
{
	fun withUnits(builder: List<DispatchUnit<N, *>>.() -> List<DispatchUnit<N, *>>) = copy(currentUnits = currentUnits.builder())

	fun withOperation(operation: TreeNodeOperation<N>) = copy(operations = operations + operation)
}

internal fun <N> dispatchDiff(currentNodes: List<TreeNode<N, *>>,
                              newElements: List<UElement<N, *>>): List<TreeNodeOperation<N>>
{
	val initialUnits = currentNodes.map { DispatchUnit.Existing(it) }
	val targetUnits = newElements.map { DispatchUnit.New(it) }

	return DispatchingState(initialUnits, emptyList())
			.dispatchUnits(targetUnits)
			.destroyExcessiveUnits(targetUnits.size)
			.operations
}

private fun <N> DispatchingState<N>.dispatchUnits(targetUnits: List<DispatchUnit.New<N, *>>) =
		targetUnits.foldIndexed(this) { targetIndex, state, targetUnit ->
			state.dispatchUnit(targetUnit, targetIndex)
		}

private fun <N, P : UProps> DispatchingState<N>.dispatchUnit(
		targetUnit: DispatchUnit.New<N, P>,
		targetIndex: Int,
		current: IndexedValue<DispatchUnit<N, P>>? = currentUnits.findCorrespondingUnitWithIndex(targetUnit)
) = when
{
	current == null -> unitCreated(targetUnit, targetIndex)
	current.index > targetIndex -> unitDetached(current.value.asExisting)
								  .unitAttached(current.value.asExisting, targetIndex)
								  .unitUpdated(current.value.asExisting, targetUnit.props)
	current.index < targetIndex -> throw IllegalStateException("Dispatching error (maybe keys are not unique?)")
	else -> unitUpdated(current.value.asExisting, targetUnit.props)
}

@Suppress("UNCHECKED_CAST")
private fun <N, P : UProps> List<DispatchUnit<N, *>>.findCorrespondingUnitWithIndex(unit: DispatchUnit<N, P>) =
		withIndex().find { it.value.isCorrespondingTo(unit) } as IndexedValue<DispatchUnit<N, P>>?

private fun <N> DispatchUnit<N, *>.isCorrespondingTo(unit: DispatchUnit<N, *>) = when
{
	propsClass != unit.propsClass -> false
	key == AutoKey && unit.key != AutoKey -> false
	key == AutoKey && unit.key == AutoKey -> true // TODO Handle case of multiple AutoKeys in one component
	else -> unit.key == key
}

private fun <N> DispatchingState<N>.destroyExcessiveUnits(limit: Int) =
		currentUnits.drop(limit).fold(this) { state, unit ->
			state.unitDestroyed(unit.asExisting)
		}

private fun <N, P : UProps> DispatchingState<N>.unitCreated(unit: DispatchUnit.New<N, P>, targetIndex: Int) = this
		.withUnits { elementInserted(unit, targetIndex) }
		.withOperation(CreateAndAttachNode(unit.element, targetIndex))

private fun <N, P : UProps> DispatchingState<N>.unitUpdated(unit: DispatchUnit.Existing<N, P>, props: P) = this
		.withOperation(TreeNodeOperation.UpdateNode(unit.node, props))

private fun <N, P : UProps> DispatchingState<N>.unitDestroyed(unit: DispatchUnit.Existing<N, P>) = this
		.withUnits { elementRemoved(unit) }
		.withOperation(TreeNodeOperation.DestroyAndDetachNode(unit.node))

private fun <N, P : UProps> DispatchingState<N>.unitAttached(unit: DispatchUnit.Existing<N, P>, targetIndex: Int) = this
		.withUnits { elementInserted(unit, targetIndex) }
		.withOperation(TreeNodeOperation.AttachNode(unit.node, targetIndex))

private fun <N, P : UProps> DispatchingState<N>.unitDetached(unit: DispatchUnit.Existing<N, P>) = this
		.withUnits { elementRemoved(unit) }
		.withOperation(TreeNodeOperation.DetachNode(unit.node))
