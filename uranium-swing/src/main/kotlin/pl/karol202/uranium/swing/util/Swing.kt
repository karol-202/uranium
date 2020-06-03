package pl.karol202.uranium.swing.util

import pl.karol202.uranium.core.component.UAbstractAppComponent
import pl.karol202.uranium.core.component.UAbstractComponent
import pl.karol202.uranium.core.component.UAbstractNativeComponent
import pl.karol202.uranium.core.component.UStatefulAppComponent
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.manager.RenderManager
import pl.karol202.uranium.core.render.URenderBuilder
import pl.karol202.uranium.core.render.URenderScope

object Swing

typealias SwingElement<P> = UElement<Swing, P>

typealias SwingAbstractAppComponent<P> = UAbstractAppComponent<Swing, P>

typealias SwingAbstractNativeComponent<P> = UAbstractNativeComponent<Swing, P>

typealias SwingStatefulComponent<P, S> = UStatefulAppComponent<Swing, P, S>

typealias SwingRenderScope = URenderScope<Swing>

typealias SwingRenderBuilder = URenderBuilder<Swing>

typealias SwingRenderManager<P> = RenderManager<Swing, P>

internal typealias Builder<T> = T.() -> T

internal fun renderScope() = object : URenderScope<Swing> { }
