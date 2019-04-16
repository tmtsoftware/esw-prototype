package ui.paper

import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperNs
import typings.paperLib.paperMod.Color

import scala.scalajs.js.|

class Display(radius: Int, maxRows: Int) {
  lazy val mirrors: List[Mirror]                    = new Honeycomb(20, 10).mirrors
  lazy val hexagons: Map[Int, List[RegularPolygon]] = mirrors.groupBy(_.sector).mapValues(_.map(createHexagon)).toList.toMap

  def honeyComb(): Unit = {
    hexagons
    println(mirrors.length)
    mirrors.foreach(println)
  }

  private def createHexagon(mirror: Mirror): RegularPolygon = new RegularPolygon(mirror.point, 6, radius) {
    private val defaultColor = new Color(List("#E7CFA0", "#7CC1D2", "#A97FFF")(mirror.sector % 3))
    private val clickColor   = new Color("red")

    fillColor = defaultColor
    strokeColor = "white"

    override def onClick(event: paperNs.MouseEvent): Unit | Boolean = {
      if (fillColor == defaultColor) {
        fillColor = clickColor

      } else if (fillColor == clickColor) {
        fillColor = defaultColor
      }
    }
    override def onDoubleClick(event: paperNs.MouseEvent): Unit | Boolean = {
      hexagons.getOrElse(mirror.sector, Nil).foreach(_.onClick(event))
      onClick(event)
    }
  }
}
