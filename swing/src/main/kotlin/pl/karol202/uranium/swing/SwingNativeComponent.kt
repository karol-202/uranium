package pl.karol202.uranium.swing

import pl.karol202.uranium.core.common.BaseProps
import pl.karol202.uranium.core.common.UProps
import pl.karol202.uranium.core.util.Prop
import pl.karol202.uranium.core.util.RenderBuilder
import pl.karol202.uranium.core.util.buildComponent
import pl.karol202.uranium.core.util.prop
import pl.karol202.uranium.swing.util.*

class SwingNativeComponent(private val native: SwingNative,
                           props: Props) : SwingAbstractComponent<SwingNativeComponent.Props>(props)
{
	data class Props(val baseProps: BaseProps,
	                 val children: List<SwingElement<*>> = emptyList(),
	                 val baseListeners: Prop<BaseListeners> = Prop.NoValue,
	                 val enabled: Prop<Boolean> = Prop.NoValue,
	                 val visible: Prop<Boolean> = Prop.NoValue) : UProps by baseProps
	{
		interface Provider<S : Provider<S>> : UProps
		{
			val swingProps: Props

			fun withSwingProps(builder: Props.() -> Props): S
		}
	}

	private val baseListeners = props.baseListeners.value
	private val componentListener = ComponentListenerDelegate { baseListeners?.componentListener }
	private val focusListener = FocusListenerDelegate { baseListeners?.focusListener }
	private val hierarchyBoundsListener = HierarchyBoundsListenerDelegate { baseListeners?.hierarchyBoundsListener }
	private val hierarchyListener = HierarchyListenerDelegate { baseListeners?.hierarchyListener }
	private val inputMethodListener = InputMethodListenerDelegate { baseListeners?.inputMethodListener }
	private val keyListener = KeyListenerDelegate { baseListeners?.keyListener }
	private val mouseListener = MouseListenerDelegate { baseListeners?.mouseListener }
	private val mouseMotionListener = MouseMotionListenerDelegate { baseListeners?.mouseMotionListener }
	private val mouseWheelListener = MouseWheelListenerDelegate { baseListeners?.mouseWheelListener }
	private val ancestorListener = AncestorListenerDelegate { baseListeners?.ancestorListener }
	private val vetoableChangeListener = VetoableChangeListenerDelegate { baseListeners?.vetoableChangeListener }

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
		native.removeAncestorListener(ancestorListener)
		native.removeVetoableChangeListener(vetoableChangeListener)

		parentContext.detachNative(native)
	}

	override fun RenderBuilder<SwingNative>.render()
	{
		add(props.children)
		onUpdate()
	}

	private fun onUpdate() = native.apply {
		props.enabled.ifPresent { isEnabled = it }
		props.visible.ifPresent { isVisible = it }
	}
}

private typealias NativeComponentBuilder = SwingComponentBuilder<out SwingNativeComponent.Props.Provider<*>>

fun SwingRenderBuilder.nativeComponent(native: SwingNative, props: SwingNativeComponent.Props) =
		buildComponent({ SwingNativeComponent(native, it) }, props)
fun NativeComponentBuilder.baseListeners(baseListeners: BaseListeners) =
		withProps { withSwingProps { copy(baseListeners = baseListeners.prop()) } }
fun NativeComponentBuilder.enabled(enabled: Boolean) = withProps { withSwingProps { copy(enabled = enabled.prop()) } }
fun NativeComponentBuilder.visible(visible: Boolean) = withProps { withSwingProps { copy(visible = visible.prop()) } }
