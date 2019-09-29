package pl.karol202.uranium.swing

import pl.karol202.uranium.core.common.BaseProps
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.component.component
import pl.karol202.uranium.core.util.Builder
import pl.karol202.uranium.core.util.Prop
import pl.karol202.uranium.core.util.prop
import pl.karol202.uranium.swing.util.*
import java.awt.*
import java.awt.event.*
import java.beans.PropertyChangeListener
import java.beans.VetoableChangeListener
import java.util.*
import javax.swing.border.Border
import javax.swing.event.AncestorListener

class SwingNativeComponent(private val native: SwingNative,
                           private val contextOverride: SwingContext?,
                           props: Props) : SwingAbstractComponent<SwingNativeComponent.Props>(props)
{
	companion object
	{
		fun props(key: Any) = Props(BaseProps(key))
	}

	data class Props(val baseProps: BaseProps,
	                 val children: List<SwingElement<*>> = emptyList(),
	                 val componentListener: Prop<ComponentListener> = Prop.NoValue,
	                 val focusListener: Prop<FocusListener> = Prop.NoValue,
	                 val hierarchyBoundsListener: Prop<HierarchyBoundsListener> = Prop.NoValue,
	                 val hierarchyListener: Prop<HierarchyListener> = Prop.NoValue,
	                 val inputMethodListener: Prop<InputMethodListener> = Prop.NoValue,
	                 val keyListener: Prop<KeyListener> = Prop.NoValue,
	                 val mouseListener: Prop<MouseListener> = Prop.NoValue,
	                 val mouseMotionListener: Prop<MouseMotionListener> = Prop.NoValue,
	                 val mouseWheelListener: Prop<MouseWheelListener> = Prop.NoValue,
	                 val propertyChangeListener: Prop<PropertyChangeListener> = Prop.NoValue,
	                 val containerListener: Prop<ContainerListener> = Prop.NoValue,
	                 val ancestorListener: Prop<AncestorListener> = Prop.NoValue,
	                 val vetoableChangeListener: Prop<VetoableChangeListener> = Prop.NoValue,
	                 val enabled: Prop<Boolean> = Prop.NoValue,
	                 val visible: Prop<Boolean> = Prop.NoValue,
	                 val focusable: Prop<Boolean> = Prop.NoValue,
	                 val opaque: Prop<Boolean> = Prop.NoValue,
	                 val inputMethodsEnabled: Prop<Boolean> = Prop.NoValue,
	                 val autoscrollsEnabled: Prop<Boolean> = Prop.NoValue,
	                 val doubleBuffered: Prop<Boolean> = Prop.NoValue,
	                 val requestFocus: Prop<Boolean> = Prop.NoValue,
	                 val verifyInputWhenFocusTarget: Prop<Boolean> = Prop.NoValue,
	                 val orientation: Prop<ComponentOrientation> = Prop.NoValue,
	                 val background: Prop<Color?> = Prop.NoValue,
	                 val border: Prop<Border> = Prop.NoValue,
	                 val cursor: Prop<Cursor?> = Prop.NoValue,
	                 val font: Prop<Font?> = Prop.NoValue,
	                 val foreground: Prop<Color?> = Prop.NoValue,
	                 val locale: Prop<Locale> = Prop.NoValue,
	                 val name: Prop<String> = Prop.NoValue,
	                 val tooltipText: Prop<String?> = Prop.NoValue,
	                 val alignmentX: Prop<Float> = Prop.NoValue,
	                 val alignmentY: Prop<Float> = Prop.NoValue,
	                 val bounds: Prop<Rectangle> = Prop.NoValue,
	                 val location: Prop<Point> = Prop.NoValue,
	                 val minimumSize: Prop<Dimension?> = Prop.NoValue,
	                 val maximumSize: Prop<Dimension?> = Prop.NoValue,
	                 val preferredSize: Prop<Dimension?> = Prop.NoValue,
	                 val size: Prop<Dimension> = Prop.NoValue) : UProps by baseProps,
	                                                             PropsProvider<Props>
	{
		override val swingProps = this

		override fun withSwingProps(builder: Builder<Props>) = builder()
	}

	interface PropsProvider<S : PropsProvider<S>> : UProps
	{
		val swingProps: Props

		fun withSwingProps(builder: Builder<Props>): S
	}

	override val context get() = contextOverride ?: super.context

	private val componentListener = ComponentListenerDelegate { props.componentListener.value }
	private val focusListener = FocusListenerDelegate { props.focusListener.value }
	private val hierarchyBoundsListener = HierarchyBoundsListenerDelegate { props.hierarchyBoundsListener.value }
	private val hierarchyListener = HierarchyListenerDelegate { props.hierarchyListener.value }
	private val inputMethodListener = InputMethodListenerDelegate { props.inputMethodListener.value }
	private val keyListener = KeyListenerDelegate { props.keyListener.value }
	private val mouseListener = MouseListenerDelegate { props.mouseListener.value }
	private val mouseMotionListener = MouseMotionListenerDelegate { props.mouseMotionListener.value }
	private val mouseWheelListener = MouseWheelListenerDelegate { props.mouseWheelListener.value }
	private val propertyChangeListener = PropertyChangeListenerDelegate { props.propertyChangeListener.value }
	private val containerListener = ContainerListenerDelegate { props.containerListener.value }
	private val ancestorListener = AncestorListenerDelegate { props.ancestorListener.value }
	private val vetoableChangeListener = VetoableChangeListenerDelegate { props.vetoableChangeListener.value }

	override fun onAttach(parentContext: InvalidateableSwingContext)
	{
		native.addComponentListener(componentListener)
		native.addFocusListener(focusListener)
		native.addHierarchyBoundsListener(hierarchyBoundsListener)
		native.addHierarchyListener(hierarchyListener)
		native.addInputMethodListener(inputMethodListener)
		native.addKeyListener(keyListener)
		native.addMouseListener(mouseListener)
		native.addMouseMotionListener(mouseMotionListener)
		native.addMouseWheelListener(mouseWheelListener)
		native.addPropertyChangeListener(propertyChangeListener)
		native.addContainerListener(containerListener)
		native.addAncestorListener(ancestorListener)
		native.addVetoableChangeListener(vetoableChangeListener)

		parentContext.attachNative(native)
	}

	override fun onDetach(parentContext: InvalidateableSwingContext)
	{
		native.removeComponentListener(componentListener)
		native.removeFocusListener(focusListener)
		native.removeHierarchyBoundsListener(hierarchyBoundsListener)
		native.removeHierarchyListener(hierarchyListener)
		native.removeInputMethodListener(inputMethodListener)
		native.removeKeyListener(keyListener)
		native.removeMouseListener(mouseListener)
		native.removeMouseMotionListener(mouseMotionListener)
		native.removeMouseWheelListener(mouseWheelListener)
		native.removePropertyChangeListener(propertyChangeListener)
		native.removeContainerListener(containerListener)
		native.removeAncestorListener(ancestorListener)
		native.removeVetoableChangeListener(vetoableChangeListener)

		parentContext.detachNative(native)
	}

	override fun SwingRenderBuilder.render()
	{
		+ props.children
		onUpdate()
	}

	private fun onUpdate() = native.apply {
		props.enabled.ifPresent { isEnabled = it }
		props.visible.ifPresent { isVisible = it }
		props.focusable.ifPresent { isFocusable = it }
		props.opaque.ifPresent { isOpaque = it }
		props.inputMethodsEnabled.ifPresent { enableInputMethods(it) }
		props.autoscrollsEnabled.ifPresent { autoscrolls = it }
		props.doubleBuffered.ifPresent { isDoubleBuffered = it }
		props.requestFocus.ifPresent { isRequestFocusEnabled = it }
		props.verifyInputWhenFocusTarget.ifPresent { verifyInputWhenFocusTarget = it }
		props.orientation.ifPresent { componentOrientation = it }
		props.background.ifPresent { background = it }
		props.border.ifPresent { border = it }
		props.cursor.ifPresent { cursor = it }
		props.font.ifPresent { font = it }
		props.foreground.ifPresent { foreground = it }
		props.locale.ifPresent { locale = it }
		props.name.ifPresent { name = it }
		props.tooltipText.ifPresent { toolTipText = it }
		props.alignmentX.ifPresent { alignmentX = it }
		props.alignmentY.ifPresent { alignmentY = it }
		props.bounds.ifPresent { bounds = it }
		props.location.ifPresent { location = it }
		props.minimumSize.ifPresent { minimumSize = it }
		props.maximumSize.ifPresent { maximumSize = it }
		props.preferredSize.ifPresent { preferredSize = it }
		props.size.ifPresent { size = it }
	}
}

private typealias Provider<P> = SwingNativeComponent.PropsProvider<P>
fun <P : Provider<P>> SwingElement<P>.withSwingProps(builder: Builder<SwingNativeComponent.Props>) =
		withProps { withSwingProps(builder) }

fun SwingRenderBuilder.nativeComponent(native: SwingNative, contextOverride: SwingContext? = null, props: SwingNativeComponent.Props) =
		component({ SwingNativeComponent(native, contextOverride, it) }, props)
fun <P : Provider<P>> SwingElement<P>.componentListener(listener: ComponentListener) = withSwingProps { copy(componentListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.focusListener(listener: FocusListener) = withSwingProps { copy(focusListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.hierarchyBoundsListener(listener: HierarchyBoundsListener) = withSwingProps { copy(hierarchyBoundsListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.hierarchyListener(listener: HierarchyListener) = withSwingProps { copy(hierarchyListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.inputMethodListener(listener: InputMethodListener) = withSwingProps { copy(inputMethodListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.keyListener(listener: KeyListener) = withSwingProps { copy(keyListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.mouseListener(listener: MouseListener) = withSwingProps { copy(mouseListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.mouseMotionListener(listener: MouseMotionListener) = withSwingProps { copy(mouseMotionListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.mouseWheelListener(listener: MouseWheelListener) = withSwingProps { copy(mouseWheelListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.propertyChangeListener(listener: PropertyChangeListener) = withSwingProps { copy(propertyChangeListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.containerListener(listener: ContainerListener) = withSwingProps { copy(containerListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.ancestorListener(listener: AncestorListener) = withSwingProps { copy(ancestorListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.vetoableChangeListener(listener: VetoableChangeListener) = withSwingProps { copy(vetoableChangeListener = listener.prop()) }
fun <P : Provider<P>> SwingElement<P>.enabled(enabled: Boolean) = withSwingProps { copy(enabled = enabled.prop()) }
fun <P : Provider<P>> SwingElement<P>.visible(visible: Boolean) = withSwingProps { copy(visible = visible.prop()) }
fun <P : Provider<P>> SwingElement<P>.focusable(focusable: Boolean) = withSwingProps { copy(focusable = focusable.prop()) }
fun <P : Provider<P>> SwingElement<P>.opaque(opaque: Boolean) = withSwingProps { copy(opaque = opaque.prop()) }
fun <P : Provider<P>> SwingElement<P>.inputMethodsEnabled(enabled: Boolean) = withSwingProps { copy(inputMethodsEnabled = enabled.prop()) }
fun <P : Provider<P>> SwingElement<P>.autoscrollsEnabled(enabled: Boolean) = withSwingProps { copy(autoscrollsEnabled = enabled.prop()) }
fun <P : Provider<P>> SwingElement<P>.doubleBuffered(enabled: Boolean) = withSwingProps { copy(doubleBuffered = enabled.prop()) }
fun <P : Provider<P>> SwingElement<P>.requestFocus(enabled: Boolean) = withSwingProps { copy(requestFocus = enabled.prop()) }
fun <P : Provider<P>> SwingElement<P>.verifyInputWhenFocusTarget(enabled: Boolean) = withSwingProps { copy(verifyInputWhenFocusTarget = enabled.prop()) }
fun <P : Provider<P>> SwingElement<P>.orientation(orientation: ComponentOrientation) = withSwingProps { copy(orientation = orientation.prop()) }
fun <P : Provider<P>> SwingElement<P>.background(background: Color?) = withSwingProps { copy(background = background.prop()) }
fun <P : Provider<P>> SwingElement<P>.border(border: Border) = withSwingProps { copy(border = border.prop()) }
fun <P : Provider<P>> SwingElement<P>.cursor(cursor: Cursor?) = withSwingProps { copy(cursor = cursor.prop()) }
fun <P : Provider<P>> SwingElement<P>.font(font: Font?) = withSwingProps { copy(font = font.prop()) }
fun <P : Provider<P>> SwingElement<P>.foreground(foreground: Color?) = withSwingProps { copy(foreground = foreground.prop()) }
fun <P : Provider<P>> SwingElement<P>.locale(locale: Locale) = withSwingProps { copy(locale = locale.prop()) }
fun <P : Provider<P>> SwingElement<P>.name(name: String) = withSwingProps { copy(name = name.prop()) }
fun <P : Provider<P>> SwingElement<P>.tooltipText(text: String?) = withSwingProps { copy(tooltipText = text.prop()) }
fun <P : Provider<P>> SwingElement<P>.alignmentX(alignX: Float) = withSwingProps { copy(alignmentX = alignX.prop()) }
fun <P : Provider<P>> SwingElement<P>.alignmentY(alignY: Float) = withSwingProps { copy(alignmentY = alignY.prop()) }
fun <P : Provider<P>> SwingElement<P>.bounds(bounds: Rectangle) = withSwingProps { copy(bounds = bounds.prop()) }
fun <P : Provider<P>> SwingElement<P>.location(location: Point) = withSwingProps { copy(location = location.prop()) }
fun <P : Provider<P>> SwingElement<P>.minimumSize(size: Dimension?) = withSwingProps { copy(minimumSize = size.prop()) }
fun <P : Provider<P>> SwingElement<P>.maximumSize(size: Dimension?) = withSwingProps { copy(maximumSize = size.prop()) }
fun <P : Provider<P>> SwingElement<P>.preferredSize(size: Dimension?) = withSwingProps { copy(preferredSize = size.prop()) }
fun <P : Provider<P>> SwingElement<P>.size(size: Dimension) = withSwingProps { copy(size = size.prop()) }
