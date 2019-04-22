package ui.spikes.svgjs

import scala.scalajs.js

case class Point(x: Double, y: Double) {
  def move(x: Double, y: Double) = Point(x + this.x, y + this.y)
}
case class PointArray(points: List[Point]) {
  def move(x: Double, y: Double) = PointArray(points.map(_.move(x, y)))
  def take(n: Int): PointArray   = PointArray(points.take(2))
}

object Adapter {
  def apply(point: Point): js.Array[Double]                     = js.Array(point.x, point.y)
  def apply(pointArray: PointArray): js.Array[js.Array[Double]] = js.Array(pointArray.points.map(apply): _*)
}
