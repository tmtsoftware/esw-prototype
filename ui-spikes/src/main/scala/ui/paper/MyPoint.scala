package ui.paper

case class MyPoint(x: Double, y: Double) {
  def hexagonVertices(radius: Double, rotation: Double): List[MyPoint] =
    (0 until 6).map(i => shift(radius, i * Math.PI / 3 + rotation)).toList

  def shift(radius: Double, angle: Double): MyPoint = MyPoint(
    x + radius * Math.cos(angle),
    y + radius * Math.sin(angle)
  )

  override def equals(obj: Any): Boolean = obj match {
    case x: MyPoint => underlying == x.underlying
    case _          => false
  }

  override def hashCode(): Int = underlying.hashCode()

  private def underlying: (Long, Long) = (x.round, y.round)
}
