package ui.paper

import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperMod.{Color, Point, PointText, ^ => Paper}
import typings.paperLib.paperNs

import scala.scalajs.js.|
import scala.util.Random

class Honeycomb(radius: Int) {
  private val Length = Math.cos(Math.PI / 6) * radius
  private val Center = Paper.view.center

  println((radius * 2, Length * 2))

  (1 to 6).foreach { i =>
    createHexagon(pointOffCenter(i * 60, Length * 2))
  }

  (1 to 6).foreach { i =>
    createHexagon(pointOffCenter(i * 60, Length * 4))
  }

  (1 to 6).foreach { i =>
    createHexagon(pointOffCenter(i * 60 + 30, radius * 3))
  }

  new PointText(pointOffCenter(_angle = 0, _length = Length * 2)) {
    content = "."
    strokeWidth = 2
    strokeColor = "black"
  }
  new PointText(pointOffCenter(_angle = 0, _length = radius * 2)) {
    content = "."
    strokeWidth = 2
    strokeColor = "red"
  }

  new PointText(Center.add(new Point(0.0, -180))) {
    content = "Click on any hexagon to change the color of all"
    justification = "center"
  }

  def createHexagon(point: Point): RegularPolygon = new RegularPolygon(point, 6, radius) {
    fillColor = "orange"
    strokeColor = "black"
    override def onClick(event: paperNs.MouseEvent): Unit | Boolean = {
      val color = new Color(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
      fillColor = color
    }
  }

  def pointOffCenter(_angle: Double, _length: Double): Point = {
    val delta = new Point() {
      angle = _angle
      length = _length
    }
    val center = Center.add(delta)
    new Point(center)
  }
}
