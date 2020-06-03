package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.PropsProvider
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement

interface UComponent<N, P : UProps> : PropsProvider<P>
{
	fun create(context: ComponentContext)

	fun destroy()

	fun render(): List<UElement<N, *>>

	fun onUpdate(previousProps: P?) { }

	fun modifyPropsInternal(props: P)
}
