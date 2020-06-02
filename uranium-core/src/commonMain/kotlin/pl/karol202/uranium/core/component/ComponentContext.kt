package pl.karol202.uranium.core.component

interface ComponentContext
{
	fun invalidate()
}

fun componentContext(invalidate: () -> Unit) = object : ComponentContext
{
	override fun invalidate() = invalidate()
}
