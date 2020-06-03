package pl.karol202.uranium.swing.util

internal fun <T> T.update(block: T.() -> Unit) = block()
