package ui.paper

case class Point(x: Double, y: Double) {
  def hexagonVertices(radius: Double, rotation: Double): List[Point] =
    (0 until 6).map(i => shift(radius, i * Math.PI / 3 + rotation)).toList

  def shift(radius: Double, angle: Double): Point = add(Point.polar(radius, angle))

  def add(point: Point) = Point(x + point.x, y + point.y)

  override def equals(obj: Any): Boolean = obj match {
    case x: Point => underlying == x.underlying
    case _        => false
  }

  override def hashCode(): Int = underlying.hashCode()

  private def underlying: (Long, Long) = (x.round, y.round)
}

object Point {
  def polar(radius: Double, angle: Double) = Point(
    radius * Math.cos(angle),
    radius * Math.sin(angle)
  )
}
