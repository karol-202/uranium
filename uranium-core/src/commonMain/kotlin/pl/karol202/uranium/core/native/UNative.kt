package pl.karol202.uranium.core.native

/**
 * Native is an object referring to platform's entity such as view, control or DOM node.
 * Tree of natives is a final result of committing phase.
 *
 * [UNative] is an interface, that all natives must implement.
 * Every platform implementation has to have its own natives implementations.
 *
 * @param N native marker
 */
interface UNative<N>

/**
 * [UNativeContainer] is a special [UNative] that itself can contain other natives (maybe native containers).
 *
 * On some platforms all natives implement [UNativeContainer] (for example Swing, where JComponents is used
 * as by native objects and all JComponents are containers), and on other platforms this is not the case.
 *
 * @param N native marker
 */
interface UNativeContainer<N> : UNative<N>
{
	/**
	 * Inserts given native as a child of this container at the given position
	 * and shifts all the natives at indexes greater or equal to [index] to the right.
	 *
	 * @param native native to insert
	 * @param index index the native will be inserted at
	 */
	fun attach(native: UNative<N>, index: Int)

	/**
	 * Removes given native from this container,
	 * and shifts all the natives at indexes greater than [native]'s index to the left.
	 *
	 * @param native native to be removed
	 */
	fun detach(native: UNative<N>)
}

internal fun <N> UNative<N>.asNode() = UNativeNode(this)
