package pl.karol202.uranium.swing.util

sealed class Prop<out T>
{
    class Value<T>(override val value: T) : Prop<T>()
    {
        override fun ifPresent(block: (T) -> Unit) = block(value)
    }

    object NoValue : Prop<Nothing>()
    {
        override val value = null

        override fun ifPresent(block: (Nothing) -> Unit) { }
    }

    abstract val value: T?

    abstract fun ifPresent(block: (T) -> Unit)
}

fun <T> T.prop() = Prop.Value(this)
