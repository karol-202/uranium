package pl.karol202.uranium.swing.native

import pl.karol202.uranium.core.common.AutoKey
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.element.UElement
import pl.karol202.uranium.core.element.component
import pl.karol202.uranium.core.render.URenderScope
import pl.karol202.uranium.swing.util.*
import java.awt.*
import java.awt.event.*
import java.beans.PropertyChangeListener
import java.beans.VetoableChangeListener
import java.util.*
import javax.swing.JComponent
import javax.swing.border.Border
import javax.swing.event.AncestorListener

class SwingNativeComponent(private val nativeComponent: JComponent,
                           initialProps: Props) : SwingAbstractNativeComponent<SwingNativeComponent.Props>(initialProps)
{
	data class Props(override val key: Any = AutoKey,
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
	                 val size: Prop<Dimension> = Prop.NoValue) : UProps,
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

	override val native = SwingNative.container(nativeComponent)

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

	override fun onCreate() = nativeComponent.update {
		addComponentListener(componentListener)
		addFocusListener(focusListener)
		addHierarchyBoundsListener(hierarchyBoundsListener)
		addHierarchyListener(hierarchyListener)
		addInputMethodListener(inputMethodListener)
		addKeyListener(keyListener)
		addMouseListener(mouseListener)
		addMouseMotionListener(mouseMotionListener)
		addMouseWheelListener(mouseWheelListener)
		addPropertyChangeListener(propertyChangeListener)
		addContainerListener(containerListener)
		addAncestorListener(ancestorListener)
		addVetoableChangeListener(vetoableChangeListener)
	}

	override fun onDestroy() = nativeComponent.update {
		removeComponentListener(componentListener)
		removeFocusListener(focusListener)
		removeHierarchyBoundsListener(hierarchyBoundsListener)
		removeHierarchyListener(hierarchyListener)
		removeInputMethodListener(inputMethodListener)
		removeKeyListener(keyListener)
		removeMouseListener(mouseListener)
		removeMouseMotionListener(mouseMotionListener)
		removeMouseWheelListener(mouseWheelListener)
		removePropertyChangeListener(propertyChangeListener)
		removeContainerListener(containerListener)
		removeAncestorListener(ancestorListener)
		removeVetoableChangeListener(vetoableChangeListener)
	}

	override fun URenderScope<Swing>.render() = props.children

	override fun onUpdate(previousProps: Props?) = nativeComponent.update {
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

internal fun SwingRenderScope.nativeComponent(nativeComponent: () -> JComponent,
                                              props: SwingNativeComponent.Props) =
		component({ SwingNativeComponent(nativeComponent(), it) }, props)

private typealias SNCProvider<P> = SwingNativeComponent.PropsProvider<P>
fun <P : SNCProvider<P>> SwingElement<P>.withSwingProps(builder: Builder<SwingNativeComponent.Props>) = withProps { withSwingProps(builder) }
internal fun <P : SNCProvider<P>> SwingElement<P>.children(children: List<SwingElement<*>>) = withSwingProps { copy(children = children) }
fun <P : SNCProvider<P>> SwingElement<P>.componentListener(listener: ComponentListener) = withSwingProps { copy(componentListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.focusListener(listener: FocusListener) = withSwingProps { copy(focusListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.hierarchyBoundsListener(listener: HierarchyBoundsListener) = withSwingProps { copy(hierarchyBoundsListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.hierarchyListener(listener: HierarchyListener) = withSwingProps { copy(hierarchyListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.inputMethodListener(listener: InputMethodListener) = withSwingProps { copy(inputMethodListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.keyListener(listener: KeyListener) = withSwingProps { copy(keyListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.mouseListener(listener: MouseListener) = withSwingProps { copy(mouseListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.mouseMotionListener(listener: MouseMotionListener) = withSwingProps { copy(mouseMotionListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.mouseWheelListener(listener: MouseWheelListener) = withSwingProps { copy(mouseWheelListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.propertyChangeListener(listener: PropertyChangeListener) = withSwingProps { copy(propertyChangeListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.containerListener(listener: ContainerListener) = withSwingProps { copy(containerListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.ancestorListener(listener: AncestorListener) = withSwingProps { copy(ancestorListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.vetoableChangeListener(listener: VetoableChangeListener) = withSwingProps { copy(vetoableChangeListener = listener.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.enabled(enabled: Boolean) = withSwingProps { copy(enabled = enabled.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.visible(visible: Boolean) = withSwingProps { copy(visible = visible.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.focusable(focusable: Boolean) = withSwingProps { copy(focusable = focusable.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.opaque(opaque: Boolean) = withSwingProps { copy(opaque = opaque.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.inputMethodsEnabled(enabled: Boolean) = withSwingProps { copy(inputMethodsEnabled = enabled.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.autoscrollsEnabled(enabled: Boolean) = withSwingProps { copy(autoscrollsEnabled = enabled.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.doubleBuffered(enabled: Boolean) = withSwingProps { copy(doubleBuffered = enabled.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.requestFocus(enabled: Boolean) = withSwingProps { copy(requestFocus = enabled.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.verifyInputWhenFocusTarget(enabled: Boolean) = withSwingProps { copy(verifyInputWhenFocusTarget = enabled.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.orientation(orientation: ComponentOrientation) = withSwingProps { copy(orientation = orientation.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.background(background: Color?) = withSwingProps { copy(background = background.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.border(border: Border) = withSwingProps { copy(border = border.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.cursor(cursor: Cursor?) = withSwingProps { copy(cursor = cursor.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.font(font: Font?) = withSwingProps { copy(font = font.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.foreground(foreground: Color?) = withSwingProps { copy(foreground = foreground.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.locale(locale: Locale) = withSwingProps { copy(locale = locale.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.name(name: String) = withSwingProps { copy(name = name.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.tooltipText(text: String?) = withSwingProps { copy(tooltipText = text.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.alignmentX(alignX: Float) = withSwingProps { copy(alignmentX = alignX.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.alignmentY(alignY: Float) = withSwingProps { copy(alignmentY = alignY.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.bounds(bounds: Rectangle) = withSwingProps { copy(bounds = bounds.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.location(location: Point) = withSwingProps { copy(location = location.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.minimumSize(size: Dimension?) = withSwingProps { copy(minimumSize = size.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.maximumSize(size: Dimension?) = withSwingProps { copy(maximumSize = size.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.preferredSize(size: Dimension?) = withSwingProps { copy(preferredSize = size.prop()) }
fun <P : SNCProvider<P>> SwingElement<P>.size(size: Dimension) = withSwingProps { copy(size = size.prop()) }
