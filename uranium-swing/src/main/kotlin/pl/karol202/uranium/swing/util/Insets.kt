package pl.karol202.uranium.swing.util

data class Insets(val left: Int = 0,
                  val top: Int = 0,
                  val right: Int = 0,
                  val bottom: Int = 0)
{
	fun toAWTInsets() = java.awt.Insets(left, top, right, bottom)
}
