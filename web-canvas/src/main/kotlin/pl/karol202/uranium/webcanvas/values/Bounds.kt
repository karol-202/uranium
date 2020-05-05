package pl.karol202.uranium.webcanvas.values

data class Bounds(val start: Vector,
                  val end: Vector)
{
	val size = end - start

	val x get() = start.x
	val y get() = start.y
	val width get() = size.x
	val height get() = size.x

	constructor(x: Double, y: Double, width: Double, height: Double) : this(Vector(x, y), Vector(x + width, y + height))

	operator fun contains(vector: Vector) =
			vector.x >= start.x && vector.y >= start.y && vector.x <= end.x && vector.y <= end.y
}