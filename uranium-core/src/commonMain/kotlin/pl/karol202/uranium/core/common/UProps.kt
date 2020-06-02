package pl.karol202.uranium.core.common

interface UProps : UKeyProvider

data class BasicProps(override val key: Any) : UProps

interface UPropsProvider<P : UProps> : UKeyProvider
{
	val props: P

	override val key get() = props.key
}
