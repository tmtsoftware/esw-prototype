package ui.paper

import typings.paperLib.paperMod.Color
import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperNs

import scala.scalajs.js.|

class Display(radius: Int, maxRows: Int) {
  lazy val hexagons: List[Hexagon]                          = new HoneycombFactory(radius, maxRows).create().trimmedHexagons
  lazy val mirrors: List[Mirror]                            = hexagons.map(m => Mirror(m, createHexagon(m)))
  lazy val displaysBySector: Map[Int, List[RegularPolygon]] = mirrors.groupBy(_.hexagon.sector).mapValues(_.map(_.display))
  lazy val displaysByRow: Map[Int, List[RegularPolygon]]    = mirrors.groupBy(_.hexagon.row).mapValues(_.map(_.display))

  def honeyComb(): Unit = {
    mirrors
    println(hexagons.length)
    hexagons.foreach(println)
  }

  private def createHexagon(mirror: Hexagon): RegularPolygon = new RegularPolygon(mirror.point, 6, radius) {
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
      displaysByRow.getOrElse(mirror.row, Nil).foreach(_.onClick(event))
      onClick(event)
    }
  }
}

case class Mirror(hexagon: Hexagon, display: RegularPolygon)
