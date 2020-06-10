package pl.karol202.uranium.core.render

import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.util.PlusBuilder
import pl.karol202.uranium.core.util.PlusBuilderImpl

/**
 * Interface serving as a scope for *render builder functions*.
 * *Render builder functions* allows to render any number of [UElement]s.
 * *Render builder function* has [URenderBuilder] as receiver and returns [Unit].
 * *Render builder function* can have any number of arguments,
 * but the issue from [URenderScope] applies.
 *
 * Because of [URenderBuilder] being a [PlusBuilder], having it as a receiver
 * allows to add elements to the builder using unary plus:
 *     + button(text = "My text")
 *
 * @param N native marker
 */
@RenderDsl
interface URenderBuilder<N> : PlusBuilder<UElement<N, *>>, URenderScope<N>

private class URenderBuilderImpl<N> : PlusBuilderImpl<UElement<N, *>>(), URenderBuilder<N>

/**
 * [render] for 0-arg *render builder function*. Creates a builder, renders element to it and returns them.
 */
fun <N> (URenderBuilder<N>.() -> Unit).render() = URenderBuilderImpl<N>().also(this).elements

/**
 * [render] for 1-arg *render builder function*. Creates a builder, renders element to it and returns them.
 */
fun <N, A1> (URenderBuilder<N>.(A1) -> Unit).render(arg1: A1) =
		URenderBuilderImpl<N>().also { it.this(arg1) }.elements

/**
 * [render] for 2-arg *render builder function*. Creates a builder, renders element to it and returns them.
 */
fun <N, A1, A2> (URenderBuilder<N>.(A1, A2) -> Unit).render(arg1: A1, arg2: A2) =
		URenderBuilderImpl<N>().also { it.this(arg1, arg2) }.elements

/**
 * [render] for 3-arg *render builder function*. Creates a builder, renders element to it and returns them.
 */
fun <N, A1, A2, A3> (URenderBuilder<N>.(A1, A2, A3) -> Unit).render(arg1: A1, arg2: A2, arg3: A3) =
		URenderBuilderImpl<N>().also { it.this(arg1, arg2, arg3) }.elements
