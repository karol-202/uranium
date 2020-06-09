package pl.karol202.uranium.core.render

import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.util.PlusBuilder
import pl.karol202.uranium.core.util.PlusBuilderImpl

@RenderDsl
interface URenderBuilder<N> : PlusBuilder<UElement<N, *>>, URenderScope<N>

private class URenderBuilderImpl<N> : PlusBuilderImpl<UElement<N, *>>(), URenderBuilder<N>

fun <N> (URenderBuilder<N>.() -> Unit).render() = URenderBuilderImpl<N>().also(this).elements

fun <N, A1> (URenderBuilder<N>.(A1) -> Unit).render(arg1: A1) =
		URenderBuilderImpl<N>().also { it.this(arg1) }.elements

fun <N, A1, A2> (URenderBuilder<N>.(A1, A2) -> Unit).render(arg1: A1, arg2: A2) =
		URenderBuilderImpl<N>().also { it.this(arg1, arg2) }.elements

fun <N, A1, A2, A3> (URenderBuilder<N>.(A1, A2, A3) -> Unit).render(arg1: A1, arg2: A2, arg3: A3) =
		URenderBuilderImpl<N>().also { it.this(arg1, arg2, arg3) }.elements
