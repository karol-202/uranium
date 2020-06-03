package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.native.UNative

interface UNativeComponent<N, P : UProps> : UComponent<N, P>
{
	val native: UNative<N>
}
