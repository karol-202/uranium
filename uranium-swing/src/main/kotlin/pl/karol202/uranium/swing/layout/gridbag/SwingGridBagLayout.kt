package pl.karol202.uranium.swing.layout.gridbag

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.layout.SwingLayout
import pl.karol202.uranium.swing.layout.constraint
import pl.karol202.uranium.swing.layout.layout
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.Container
import java.awt.GridBagLayout
import java.awt.LayoutManager

class SwingGridBagLayout(props: Props) : SwingAbstractAppComponent<SwingGridBagLayout.Props>(props)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val content: List<GridBagCell> = emptyList()) : UProps,
	                                                                 SwingNativeComponent.PropsProvider<Props>,
	                                                                 PropsProvider<Props>
	{
		override val gridBagLayoutProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())

		override fun withGridBagLayoutProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val gridBagLayoutProps: Props

		fun withGridBagLayoutProps(builder: Builder<Props>): S
	}

	override fun SwingRenderScope.render() = layout(swingProps = props.swingProps,
	                                                content = renderContent(),
	                                                layoutUpdater = ::updateLayout)

	private fun SwingRenderScope.renderContent() =
			props.content.map { constraint(constraint = it.constraints, content = it.content) }

	private fun updateLayout(container: Container, layout: LayoutManager?) = layout as? GridBagLayout ?: GridBagLayout()
}

fun SwingRenderScope.gridBagLayout(key: Any = AutoKey,
                                   block: SwingGridBagBuilder.() -> Unit) =
		gridBagLayout(SwingGridBagLayout.Props(key)).content(block)

internal fun SwingRenderScope.gridBagLayout(props: SwingGridBagLayout.Props) = component(::SwingGridBagLayout, props)

private typealias SGBLProvider<P> = SwingGridBagLayout.PropsProvider<P>
fun <P : SGBLProvider<P>> SwingElement<P>.withGridBagLayoutProps(builder: Builder<SwingGridBagLayout.Props>) =
		withProps { withGridBagLayoutProps(builder) }
fun <P : SGBLProvider<P>> SwingElement<P>.content(block: SwingGridBagBuilder.() -> Unit) =
		withGridBagLayoutProps { copy(content = block.render()) }
