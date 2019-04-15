package ui.paper

import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperMod.{Color, PointText, ^ => Paper}
import typings.paperLib.paperNs.{MouseEvent, Point}

import scala.scalajs.js
import scala.scalajs.js.|

class Hexagoan {
  val Radius = 10

  private var hexagoans = js.Array[RegularPolygon]()

  private val point = Paper.view.center

  println(point)

  private val Length = Math.cos(Math.PI / 6) * Radius

//  (1 to 6).foreach { i =>
//    val center = point.add(
//      new Point(
//        js.Dynamic.literal(
//          "angle"  -> i * 60,
//          "length" -> Length * 2
//        )
//      )
//    )
//    hexagoans.push(createHexagoan(center))
//  }

  new PointText(
    js.Dynamic.literal(
      content = "Click on any hexagon to change the color of all",
//      point = point.add(new Point(0, -180)),
      justification = "center"
    )
  )

  def createHexagoan(point: Point): RegularPolygon = {
    val polygon = new RegularPolygon(point, 6, Radius) {
//      override def onClick(event: MouseEvent): Unit | Boolean = changeColor()
    }
    polygon.fillColor = "orange"
    polygon.strokeColor = "black"
    polygon
  }

  def changeColor(): Unit = {
    val color = new Color("red")
    hexagoans.foreach { h =>
      h.fillColor = color
    }
  }
}
