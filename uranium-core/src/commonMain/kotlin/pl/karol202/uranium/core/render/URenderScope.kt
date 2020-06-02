package pl.karol202.uranium.core.render

import pl.karol202.uranium.core.element.UElement
import kotlin.jvm.JvmName

interface URenderScope<N>

internal fun <N> renderScope() = object : URenderScope<N> { }

fun <N> (URenderScope<N>.() -> UElement<N, *>).render() = renderScope<N>().this()

@JvmName("renderNullable")
fun <N> (URenderScope<N>.() -> UElement<N, *>?).render() = renderScope<N>().this()
