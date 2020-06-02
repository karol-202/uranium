package pl.karol202.uranium.core.native

interface UNative<N>

interface UNativeContainer<N> : UNative<N>
{
	// Inserts native at given position, shifts rest of nodes
	fun attach(native: UNative<N>, index: Int)

	fun detach(native: UNative<N>)
}

internal fun <N> UNative<N>.asNode() = UNativeNode(this)
