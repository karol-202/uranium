package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UPropsProvider
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.manager.RenderManager
import pl.karol202.uranium.core.native.UNative

/**
 * Components are basic building blocks for all *uranium* applications,
 * they form a tree structure, every component can have any number of children component
 * or may have no children.
 *
 * Every component has its own lifecycle:
 *
 * |------------------|
 *
 * |   Instantiated   |
 *
 * |------------------|
 *
 *          |   create() / onCreate()
 *
 *         \/
 *
 *   |-------------|
 *
 *   |   Created   |   render() and onUpdate() are called
 *
 *   |-------------|
 *
 *         |   destroy() / onDestroy()
 *
 *        \/
 *
 *  |---------------|
 *
 *  |   Destroyed   |
 *
 *  |---------------|
 *
 *  @param N native marker
 *  @param P props type
 */
interface UComponent<N, P : UProps> : UPropsProvider<P>
{
	/**
	 * Property returning [UNative] of this component or null.
	 *
	 * Component can provide native (object referring to platform's view/control/component)
	 * that will be used in place of the component in committing phase.
	 * If no native provided, natives of children component (or their children recursively
	 * in case of lack of natives) will be used in committing phase.
	 *
	 * For example following hierarchy of components:
	 *
	 *                  Component
	 *
	 *                (with native)
	 *
	 *                  /        \
	 *
	 *            Component      Component
	 *
	 *           (no native)   (with native)
	 *
	 *            /        \
	 *
	 *    Component        Component
	 *
	 *  (with native)    (with native)
	 *
	 *                        |
	 *
	 *                     Component
	 *
	 *                   (with native)
	 *
	 * will be transformed to the following hierarchy of natives:
	 *
	 *             Native
	 *
	 *         /     |     \
	 *
	 *   Native   Native   Native
	 *
	 *               |
	 *
	 *            Native
	 */
	val native: UNative<N>?

	/**
	 * Method called after instantiation of a component
	 *
	 * @param context [ComponentContext] for lifecycle callbacks, such as invalidation
	 */
	fun create(context: ComponentContext)

	/**
	 * Method called when a component is no longer used and is being detached from component hierarchy
	 */
	fun destroy()

	/**
	 * Method used in rendering phase to construct component hierarchy.
	 * Called in one of the following cases:
	 * - the component has just been created and attached to hierarchy
	 * - the component is root and its props have been changed by [RenderManager]
	 * - props have been changed by parent, whose [render] has been called for one of these reasons
	 *   and [needsUpdate] of this component returned true
 	 * - component has been invalidated, for example in case of state change
	 *
	 * Implementation of [render] method should be pure:
	 * - should not execute any side effects
	 * - should always return the same result for given props
	 *
	 * @return list of [UElement], that will be used to instantiate children
	 */
	fun render(): List<UElement<N, *>>

	/**
	 * Called always after [render]. Allows to execute logic or change state.
	 *
	 * @param previousProps props the component had before updating:
	 * - null if this is first invocation of [onUpdate]
	 * - equal to the current props if there was no props change
	 * - unequal to the current props if there was a props change
	 */
	fun onUpdate(previousProps: P?) { }

	/**
	 * Called if props have been changes (instance comparision) to check
	 * whether [render] and [onUpdate] should be called
	 *
	 * @param newProps new props value
	 * @return true if component should be rerendered, false otherwise
	 */
	fun needsUpdate(newProps: P): Boolean

	/**
	 * Used internally to update component's props, do not call yourself!
	 *
	 * @param props new props
	 */
	fun modifyPropsInternal(props: P)
}
