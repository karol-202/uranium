package pl.karol202.uranium.swing.util

import java.awt.event.*
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.VetoableChangeListener
import javax.swing.event.*

class ComponentListenerDelegate(private val listenerSupplier: () -> ComponentListener?) : ComponentListener
{
	override fun componentMoved(e: ComponentEvent?) = listenerSupplier()?.componentMoved(e) ?: Unit
	override fun componentResized(e: ComponentEvent?) = listenerSupplier()?.componentResized(e) ?: Unit
	override fun componentHidden(e: ComponentEvent?) = listenerSupplier()?.componentHidden(e) ?: Unit
	override fun componentShown(e: ComponentEvent?) = listenerSupplier()?.componentShown(e) ?: Unit
}

class FocusListenerDelegate(private val listenerSupplier: () -> FocusListener?) : FocusListener
{
	override fun focusLost(e: FocusEvent?) = listenerSupplier()?.focusLost(e) ?: Unit
	override fun focusGained(e: FocusEvent?) = listenerSupplier()?.focusGained(e) ?: Unit
}

class HierarchyBoundsListenerDelegate(private val listenerSupplier: () -> HierarchyBoundsListener?) : HierarchyBoundsListener
{
	override fun ancestorMoved(e: HierarchyEvent?) = listenerSupplier()?.ancestorMoved(e) ?: Unit
	override fun ancestorResized(e: HierarchyEvent?) = listenerSupplier()?.ancestorResized(e) ?: Unit
}

class HierarchyListenerDelegate(private val listenerSupplier: () -> HierarchyListener?) : HierarchyListener
{
	override fun hierarchyChanged(e: HierarchyEvent?) = listenerSupplier()?.hierarchyChanged(e) ?: Unit
}

class InputMethodListenerDelegate(private val listenerSupplier: () -> InputMethodListener?) : InputMethodListener
{
	override fun caretPositionChanged(e: InputMethodEvent?) = listenerSupplier()?.caretPositionChanged(e) ?: Unit
	override fun inputMethodTextChanged(e: InputMethodEvent?) = listenerSupplier()?.inputMethodTextChanged(e) ?: Unit
}

class KeyListenerDelegate(private val listenerSupplier: () -> KeyListener?) : KeyListener
{
	override fun keyTyped(e: KeyEvent?) = listenerSupplier()?.keyTyped(e) ?: Unit
	override fun keyPressed(e: KeyEvent?) = listenerSupplier()?.keyPressed(e) ?: Unit
	override fun keyReleased(e: KeyEvent?) = listenerSupplier()?.keyReleased(e) ?: Unit
}

class MouseListenerDelegate(private val listenerSupplier: () -> MouseListener?) : MouseListener
{
	override fun mouseReleased(e: MouseEvent?) = listenerSupplier()?.mouseReleased(e) ?: Unit
	override fun mouseEntered(e: MouseEvent?) = listenerSupplier()?.mouseEntered(e) ?: Unit
	override fun mouseClicked(e: MouseEvent?) = listenerSupplier()?.mouseClicked(e) ?: Unit
	override fun mouseExited(e: MouseEvent?) = listenerSupplier()?.mouseExited(e) ?: Unit
	override fun mousePressed(e: MouseEvent?) = listenerSupplier()?.mousePressed(e) ?: Unit
}

class MouseMotionListenerDelegate(private val listenerSupplier: () -> MouseMotionListener?) : MouseMotionListener
{
	override fun mouseMoved(e: MouseEvent?) = listenerSupplier()?.mouseMoved(e) ?: Unit
	override fun mouseDragged(e: MouseEvent?) = listenerSupplier()?.mouseDragged(e) ?: Unit
}

class MouseWheelListenerDelegate(private val listenerSupplier: () -> MouseWheelListener?) : MouseWheelListener
{
	override fun mouseWheelMoved(e: MouseWheelEvent?) = listenerSupplier()?.mouseWheelMoved(e) ?: Unit
}

class PropertyChangeListenerDelegate(private val listenerSupplier: () -> PropertyChangeListener?) : PropertyChangeListener
{
	override fun propertyChange(e: PropertyChangeEvent?) = listenerSupplier()?.propertyChange(e) ?: Unit
}

class ContainerListenerDelegate(private val listenerSupplier: () -> ContainerListener?) : ContainerListener
{
	override fun componentAdded(e: ContainerEvent?) = listenerSupplier()?.componentAdded(e) ?: Unit

	override fun componentRemoved(e: ContainerEvent?) = listenerSupplier()?.componentRemoved(e) ?: Unit
}

class AncestorListenerDelegate(private val listenerSupplier: () -> AncestorListener?) : AncestorListener
{
	override fun ancestorAdded(e: AncestorEvent?) = listenerSupplier()?.ancestorAdded(e) ?: Unit
	override fun ancestorMoved(e: AncestorEvent?) = listenerSupplier()?.ancestorMoved(e) ?: Unit
	override fun ancestorRemoved(e: AncestorEvent?) = listenerSupplier()?.ancestorRemoved(e) ?: Unit
}

class VetoableChangeListenerDelegate(private val listenerSupplier: () -> VetoableChangeListener?) : VetoableChangeListener
{
	override fun vetoableChange(e: PropertyChangeEvent?) = listenerSupplier()?.vetoableChange(e) ?: Unit
}

// For security reasons (JPasswordField), getText() will not get called unless listenerSupplier returns true
class TextChangeListenerDelegate(private val listenerSupplier: () -> ((String) -> Unit)?): DocumentListener
{
	override fun changedUpdate(e: DocumentEvent) = listenerSupplier()?.invoke(e.getText()) ?: Unit

	override fun insertUpdate(e: DocumentEvent) = listenerSupplier()?.invoke(e.getText()) ?: Unit

	override fun removeUpdate(e: DocumentEvent) = listenerSupplier()?.invoke(e.getText()) ?: Unit

	private fun DocumentEvent.getText() = document.getText(0, document.length)
}

class PopupListenerDelegate(private val popupShowListenerSupplier: () -> (() -> Unit)?,
                            private val popupHideListenerSupplier: () -> (() -> Unit)?,
                            private val popupCancelListenerSupplier: () -> (() -> Unit)?): PopupMenuListener
{
	override fun popupMenuWillBecomeVisible(e: PopupMenuEvent?) = popupShowListenerSupplier()?.invoke() ?: Unit

	override fun popupMenuWillBecomeInvisible(e: PopupMenuEvent?) = popupHideListenerSupplier()?.invoke() ?: Unit

	override fun popupMenuCanceled(e: PopupMenuEvent?) = popupCancelListenerSupplier()?.invoke() ?: Unit
}
