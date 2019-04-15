package ui.paper

import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperMod.{Color, Point, PointText, ^ => Paper}
import typings.paperLib.paperNs

import scala.scalajs.js.|
import scala.util.Random

class Hexagon(radius: Int) {
  private val Length = Math.cos(Math.PI / 6) * radius
  private val point  = Paper.view.center

  (1 to 6).foreach { i =>
    val delta = new Point() {
      angle = i * 60
      length = Length * 2
    }
    val center = point.add(delta)
    createHexagon(new Point(center))
  }

  new PointText(point.add(new Point(0.0, -180))) {
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
}
