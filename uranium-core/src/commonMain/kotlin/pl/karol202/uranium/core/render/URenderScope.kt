package pl.karol202.uranium.core.render

import pl.karol202.uranium.core.element.UElement
import kotlin.jvm.JvmName

/**
 * Interface serving no other purpose than serving as a scope for all rendering DSLs.
 *
 * All component-creating functions should use this interface as a receiver.
 * The purpose of this principle is not to clutter namespace with tons of component related functions.
 *
 * [URenderScope] is also used as receiver of *render functions*.
 * *Render function* is a function with [URenderScope] as receiver and returning single [UElement].
 * *Render functions* can have any number of arguments, but currently (0.2.3) there are
 * only 4 variants of [render] functions: for 0, 1, 2 or 3 arguments.
 * If you need a *render function* with more than 3 arguments,
 * you can write it yourself or file an issue in uranium project.
 *
 * @param N native marker
 */
interface URenderScope<N>

internal fun <N> renderScope() = object : URenderScope<N> { }

/**
 * [render] for 0-arg *render function*. Creates a scope and renders an element on it.
 */
fun <N> (URenderScope<N>.() -> UElement<N, *>).render() =
		renderScope<N>().this()

/**
 * [render] for 1-arg *render function*. Creates a scope and renders an element on it.
 */
fun <N, A1> (URenderScope<N>.(A1) -> UElement<N, *>).render(arg1: A1) =
		renderScope<N>().this(arg1)

/**
 * [render] for 2-arg *render function*. Creates a scope and renders an element on it.
 */
fun <N, A1, A2> (URenderScope<N>.(A1, A2) -> UElement<N, *>).render(arg1: A1, arg2: A2) =
		renderScope<N>().this(arg1, arg2)

/**
 * [render] for 3-arg *render function*. Creates a scope and renders an element on it.
 */
fun <N, A1, A2, A3> (URenderScope<N>.(A1, A2, A3) -> UElement<N, *>).render(arg1: A1, arg2: A2, arg3: A3) =
		renderScope<N>().this(arg1, arg2, arg3)

/**
 * [render] for 0-arg, nullable-returning *render function*. Creates a scope and renders an element on it.
 */
@JvmName("renderNullable")
fun <N> (URenderScope<N>.() -> UElement<N, *>?).render() =
		renderScope<N>().this()

/**
 * [render] for 1-arg, nullable-returning *render function*. Creates a scope and renders an element on it.
 */
@JvmName("renderNullable")
fun <N, A1> (URenderScope<N>.(A1) -> UElement<N, *>?).render(arg1: A1) =
		renderScope<N>().this(arg1)

/**
 * [render] for 2-arg, nullable-returning *render function*. Creates a scope and renders an element on it.
 */
@JvmName("renderNullable")
fun <N, A1, A2> (URenderScope<N>.(A1, A2) -> UElement<N, *>?).render(arg1: A1, arg2: A2) =
		renderScope<N>().this(arg1, arg2)

/**
 * [render] for 3-arg, nullable-returning *render function*. Creates a scope and renders an element on it.
 */
@JvmName("renderNullable")
fun <N, A1, A2, A3> (URenderScope<N>.(A1, A2, A3) -> UElement<N, *>?).render(arg1: A1, arg2: A2, arg3: A3) =
		renderScope<N>().this(arg1, arg2, arg3)
