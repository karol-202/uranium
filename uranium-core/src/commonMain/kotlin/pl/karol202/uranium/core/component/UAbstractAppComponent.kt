package pl.karol202.uranium.core.component

import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.core.render.renderScope

abstract class UAbstractAppComponent<N, P : UProps>(props: P) : UAbstractComponent<N, P>(props)
{
	final override fun render() = listOf(renderScope<N>().render())

	protected abstract fun URenderScope<N>.render(): UElement<N, *>
}
