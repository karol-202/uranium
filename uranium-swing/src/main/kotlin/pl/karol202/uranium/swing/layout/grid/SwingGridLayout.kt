package pl.karol202.uranium.swing.layout.grid

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.layout.layout
import pl.karol202.uranium.swing.native.SwingNativeComponent
import pl.karol202.uranium.swing.util.*
import java.awt.Container
import java.awt.GridLayout
import java.awt.LayoutManager
import kotlin.math.max

class SwingGridLayout(props: Props) : SwingAbstractAppComponent<SwingGridLayout.Props>(props)
{
	data class Props(override val key: Any = AutoKey,
	                 override val swingProps: SwingNativeComponent.Props = SwingNativeComponent.Props(),
	                 val content: List<SwingElement<*>> = emptyList(),
	                 val rows: Prop<Int> = Prop.NoValue,
	                 val columns: Prop<Int> = Prop.NoValue,
	                 val horizontalGap: Prop<Int> = Prop.NoValue,
	                 val verticalGap: Prop<Int> = Prop.NoValue) : UProps,
	                                                              SwingNativeComponent.PropsProvider<Props>,
	                                                              PropsProvider<Props>
	{
		override val gridLayoutProps = this

		override fun withSwingProps(builder: Builder<SwingNativeComponent.Props>) = copy(swingProps = swingProps.builder())

		override fun withGridLayoutProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val gridLayoutProps: Props

		fun withGridLayoutProps(builder: Builder<Props>): S
	}

	override fun URenderScope<Swing>.render() = layout(swingProps = props.swingProps,
	                                                   content = props.content,
	                                                   layoutUpdater = ::updateLayout)

	private fun updateLayout(container: Container, layout: LayoutManager?) = (layout as? GridLayout ?: GridLayout()).apply {
		props.rows.ifPresent { rows = max(it, 1) } // Workaround for prohibition of being both rows and cols equal to 0
		props.columns.ifPresent { columns = it }
		props.horizontalGap.ifPresent { hgap = it }
		props.verticalGap.ifPresent { vgap = it }
	}
}

fun SwingRenderScope.gridLayout(key: Any = AutoKey) = gridLayout(props = SwingGridLayout.Props(key))

internal fun SwingRenderScope.gridLayout(props: SwingGridLayout.Props) = component(::SwingGridLayout, props)

private typealias SGLProvider<P> = SwingGridLayout.PropsProvider<P>
fun <P : SGLProvider<P>> SwingElement<P>.withGridLayoutProps(builder: Builder<SwingGridLayout.Props>) =
		withProps { withGridLayoutProps(builder) }
fun <P : SGLProvider<P>> SwingElement<P>.contentRows(block: SwingGridRowsBuilder.() -> Unit) =
		content(SwingGridRowsBuilder().apply(block))
fun <P : SGLProvider<P>> SwingElement<P>.contentColumns(block: SwingGridColumnsBuilder.() -> Unit) =
		content(SwingGridColumnsBuilder().apply(block))
private fun <P : SGLProvider<P>> SwingElement<P>.content(builder: SwingGridBuilder) = withGridLayoutProps {
	copy(content = builder.elements, rows = builder.rowsAmount.prop(), columns = builder.columnsAmount.prop())
}
fun <P : SGLProvider<P>> SwingElement<P>.horizontalGap(gap: Int) = withGridLayoutProps { copy(horizontalGap = gap.prop()) }
fun <P : SGLProvider<P>> SwingElement<P>.verticalGap(gap: Int) = withGridLayoutProps { copy(verticalGap = gap.prop()) }
