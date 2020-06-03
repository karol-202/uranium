package pl.karol202.uranium.swing.util

import java.util.*

fun <K, V> Map<K, V>.toHashtable() = Hashtable(this)
