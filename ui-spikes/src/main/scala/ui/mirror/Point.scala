package ui.mirror

case class Point(x: Double, y: Double) {

  def shift(radius: Double, angle: Double): Point = add(Point.polar(radius, angle))
  def add(point: Point)                           = Point(x + point.x, y + point.y)

  override def equals(obj: Any): Boolean = obj match {
    case x: Point => underlying == x.underlying
    case _        => false
  }

  override def hashCode(): Int = underlying.hashCode()

  override def toString: String = underlying.toString()

  private def underlying: (Long, Long) = (x.round, y.round)
}

object Point {
  def polar(radius: Double, angle: Double) = Point(
    radius * Math.cos(angle),
    radius * Math.sin(angle)
  )
}

case class Hexagon(center: Point, radius: Double) {
  def vertices: List[Point] = generate(radius, Hexagon.VertexRotation, Hexagon.Indices)

  def allNeighbours: List[Hexagon] = neighbours(Hexagon.Indices)
  def neighbours(indices: List[Int]): List[Hexagon] = {
    generate(distance, Hexagon.NeighbourRotation, indices).map(p => copy(center = p))
  }
  def distance: Double = 2 * Math.cos(Math.PI / 6) * radius

  def generate(length: Double, rotation: Double, indices: List[Int]): List[Point] = indices.map { i =>
    center.shift(length, i * Math.PI / 3 + rotation)
  }

  override def toString: String = s"center=$center radius=$radius"
}

object Hexagon {
  val Indices: List[Int]        = (0 until 6).toList
  val NeighbourRotation: Double = 0
  val VertexRotation: Double    = NeighbourRotation + Math.PI / 6
}
