package pl.karol202.uranium.core.render

import pl.karol202.uranium.core.element.UElement
import kotlin.jvm.JvmName

interface URenderScope<N>

internal fun <N> renderScope() = object : URenderScope<N> { }

fun <N> (URenderScope<N>.() -> UElement<N, *>).render() =
		renderScope<N>().this()

fun <N, A1> (URenderScope<N>.(A1) -> UElement<N, *>).render(arg1: A1) =
		renderScope<N>().this(arg1)

fun <N, A1, A2> (URenderScope<N>.(A1, A2) -> UElement<N, *>).render(arg1: A1, arg2: A2) =
		renderScope<N>().this(arg1, arg2)

fun <N, A1, A2, A3> (URenderScope<N>.(A1, A2, A3) -> UElement<N, *>).render(arg1: A1, arg2: A2, arg3: A3) =
		renderScope<N>().this(arg1, arg2, arg3)

@JvmName("renderNullable")
fun <N> (URenderScope<N>.() -> UElement<N, *>?).render() =
		renderScope<N>().this()

@JvmName("renderNullable")
fun <N, A1> (URenderScope<N>.(A1) -> UElement<N, *>?).render(arg1: A1) =
		renderScope<N>().this(arg1)

@JvmName("renderNullable")
fun <N, A1, A2> (URenderScope<N>.(A1, A2) -> UElement<N, *>?).render(arg1: A1, arg2: A2) =
		renderScope<N>().this(arg1, arg2)

@JvmName("renderNullable")
fun <N, A1, A2, A3> (URenderScope<N>.(A1, A2, A3) -> UElement<N, *>?).render(arg1: A1, arg2: A2, arg3: A3) =
		renderScope<N>().this(arg1, arg2, arg3)
