package pl.karol202.uranium.core.common

interface UProps : KeyProvider

data class BasicProps(override val key: Any) : UProps

interface PropsProvider<P : UProps> : KeyProvider
{
	val props: P

	override val key get() = props.key
}
