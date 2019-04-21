package ui.paper

case class MyPoint(x: Double, y: Double) {
  def hexagonVertices(radius: Double, rotation: Double): List[MyPoint] =
    (0 until 6).map(i => shift(radius, i * Math.PI / 3 + rotation)).toList

  def shift(radius: Double, angle: Double): MyPoint = MyPoint(
    x + radius * Math.cos(angle),
    y + radius * Math.sin(angle)
  )

//  override def self: Any          = (x.round, y.round)
//  override def toString(): String = (x.round, y.round).toString()
}
