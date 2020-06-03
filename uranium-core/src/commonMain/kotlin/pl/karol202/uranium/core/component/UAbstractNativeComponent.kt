package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.core.render.renderScope

abstract class UAbstractNativeComponent<N, P : UProps>(props: P) : UAbstractComponent<N, P>(props), UNativeComponent<N, P>
{
	final override fun render() = renderScope<N>().render()

	protected abstract fun URenderScope<N>.render(): List<UElement<N, *>>
}
