package pl.karol202.uranium.swing.util

fun <E> List<E>.minusLast(element: E) = toMutableList().apply { removeAt(lastIndexOf(element)) }.toList()
