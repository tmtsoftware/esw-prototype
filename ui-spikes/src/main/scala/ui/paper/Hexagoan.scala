package ui.paper

import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperMod.{Color, PointText, ^ => Paper}
import typings.paperLib.paperMod.Point
import typings.paperLib.paperNs

import scala.scalajs.js
import scala.scalajs.js.|
import scala.util.Random

class Hexagoan {
  val Radius = 50

  private val Length = Math.cos(Math.PI / 6) * Radius
  private val point  = Paper.view.center

  println(point)

  private var hexagoans = js.Array[RegularPolygon]()

  (1 to 6).foreach { i =>
    val center = point.add {
      new Point() {
        angle = i * 60
        length = Length * 2
      }
    }

    hexagoans.push(createHexagoan(new Point(center)))
  }

  new PointText(point.add(new Point(0.0, -180))) {
    content = "Click on any hexagon to change the color of all"
    justification = "center"
  }

  def createHexagoan(point: Point): RegularPolygon = new RegularPolygon(point, 6, Radius) {
    fillColor = "orange"
    strokeColor = "black"
    override def onClick(event: paperNs.MouseEvent): Unit | Boolean = changeColor()
  }

  def changeColor(): Unit = {
    val color = new Color(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
    hexagoans.foreach { h =>
      h.fillColor = color
    }
  }
}
