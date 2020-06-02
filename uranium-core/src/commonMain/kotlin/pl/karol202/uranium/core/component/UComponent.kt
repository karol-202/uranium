package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UPropsProvider
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.native.UNative

interface UComponent<N, P : UProps> : UPropsProvider<P>
{
	val native: UNative<N>?

	fun create(context: ComponentContext)

	fun destroy()

	fun render(): List<UElement<N, *>>

	fun onUpdate(previousProps: P?) { }

	fun needsUpdate(newProps: P): Boolean

	fun modifyPropsInternal(props: P)
}
