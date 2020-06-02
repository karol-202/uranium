package pl.karol202.uranium.core.render

import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.util.PlusBuilder
import pl.karol202.uranium.core.util.PlusBuilderImpl

@RenderDsl
interface URenderBuilder<N> : PlusBuilder<UElement<N, *>>, URenderScope<N>

private class URenderBuilderImpl<N> : PlusBuilderImpl<UElement<N, *>>(), URenderBuilder<N>

fun <N> (URenderBuilder<N>.() -> Unit).render() = URenderBuilderImpl<N>().apply(this).elements
