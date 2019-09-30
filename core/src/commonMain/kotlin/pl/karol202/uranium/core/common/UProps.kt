package pl.karol202.uranium.core.common

interface UProps : KeyProvider

interface PropsProvider<P : UProps> : KeyProvider
{
	val props: P

	override val key get() = props.key
}
