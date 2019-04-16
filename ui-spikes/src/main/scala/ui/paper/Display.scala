package ui.paper

import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperNs

import scala.scalajs.js.|

class Display(radius: Int, maxRows: Int) {
  lazy val mirrors: List[Mirror] = new Honeycomb(20, 10).mirrors

  def honeyComb(): Unit = {
    mirrors.foreach(createHexagon)
    println(mirrors.length)
    mirrors.foreach(println)
  }

  private def createHexagon(mirror: Mirror): RegularPolygon = new RegularPolygon(mirror.point, 6, radius) {
    fillColor = List("#E7CFA0", "#7CC1D2", "#A97FFF")(mirror.sector % 3)
    strokeColor = "white"
    override def onClick(event: paperNs.MouseEvent): Unit | Boolean = {
      fillColor = "red"
    }
  }
}
