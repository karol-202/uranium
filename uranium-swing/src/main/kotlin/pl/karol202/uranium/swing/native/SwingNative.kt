package pl.karol202.uranium.swing.native

import pl.karol202.uranium.core.native.UNative
import pl.karol202.uranium.core.native.UNativeContainer
import pl.karol202.uranium.swing.util.Swing
import java.awt.Container

private fun UNative<Swing>.asSwingNative() = this as SwingNative

sealed class SwingNative : UNativeContainer<Swing>
{
	companion object
	{
		fun container(container: Container): SwingNative = SwingContainer(container)

		fun constraint(constraint: Any): SwingNative = SwingConstraint(constraint)
	}

	private class SwingContainer(private val container: Container) : SwingNative()
	{
		private val content = mutableMapOf<SwingNative, Any?>()

		override fun attach(native: UNative<Swing>, index: Int) = attach(native.asSwingNative(), index)

		fun attach(native: SwingNative, index: Int, constraint: Any? = null)
		{
			if(content.contains(native)) throw IllegalStateException("Native already attached")
			content += native to constraint

			when(native)
			{
				is SwingContainer -> container.add(native.container, constraint, index)
				is SwingConstraint -> native.attachToParent(this, index)
			}
		}

		override fun detach(native: UNative<Swing>) = detach(native.asSwingNative())

		private fun detach(native: SwingNative)
		{
			if(!content.contains(native)) throw IllegalStateException("Native not attached")
			content -= native

			when(native)
			{
				is SwingContainer -> container.remove(native.container)
				is SwingConstraint -> native.detachFromParent(this)
			}
		}
	}

	private class SwingConstraint(val constraint: Any) : SwingNative()
	{
		private data class Parent(val container: SwingContainer,
		                          val index: Int)

		private var parent: Parent? = null
		private var content: SwingNative? = null

		override fun attach(native: UNative<Swing>, index: Int)
		{
			val swingNative = native.asSwingNative()

			if(content != null) throw IllegalStateException("Native already set")
			content = swingNative

			parent?.let {
				it.container.attach(swingNative, it.index, constraint)
			}
		}

		override fun detach(native: UNative<Swing>)
		{
			val swingNative = native.asSwingNative()

			if(content != swingNative) throw IllegalStateException("Native not set")
			content = null

			parent?.container?.detach(swingNative)
		}

		fun attachToParent(parentContainer: SwingContainer, index: Int)
		{
			if(this.parent != null) throw IllegalStateException("Parent already set")

			this.parent = Parent(parentContainer, index)

			content?.let {
				parentContainer.attach(it, index, constraint)
			}
		}

		fun detachFromParent(parentContainer: SwingContainer)
		{
			if(this.parent?.container != parentContainer) throw IllegalStateException("Parent not set")

			this.parent = null

			content?.let {
				parentContainer.detach(it)
			}
		}
	}
}
