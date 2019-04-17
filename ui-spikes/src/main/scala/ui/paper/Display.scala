package ui.paper

import com.raquo.airstream.signal.{Signal, Var}
import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperNs

import scala.scalajs.js.|

class Display(radius: Int, maxRows: Int) extends MyOwner {
  lazy val hexagons: List[Hexagon]                 = new HoneycombFactory(radius, maxRows).create().trimmedHexagons
  lazy val mirrors: List[Mirror]                   = hexagons.map(hexagon => new Mirror(hexagon))
  lazy val mirrorsByRow: Map[Int, List[Mirror]]    = mirrors.groupBy(_.hexagon.row)
  lazy val mirrorByPosition: Map[Position, Mirror] = mirrors.map(m => m.hexagon.position -> m).toMap

  private lazy val externalService = new ExternalService

  def render(): Unit = {
    mirrors
    println(hexagons.length)
    hexagons.foreach(println)

    externalService.positions.foreach { position =>
      mirrorByPosition.get(position).foreach { mirror =>
        mirror.click()
      }
    }
  }

  val Colors = List("#E7CFA0", "#7CC1D2", "#A97FFF")

  class Mirror(val hexagon: Hexagon) extends MyOwner {
    def click(): Unit = clicked.set(!clicked.now())

    private val clicked = Var(false)

    val color: Signal[String] = clicked.signal.map {
      case false => Colors(hexagon.sector % 3)
      case true  => "red"
    }

    private lazy val mirrorsWithSameRow = mirrorsByRow.getOrElse(hexagon.row, Nil)

    new RegularPolygon(hexagon.point, 6, radius) {
      color.foreach(x => fillColor = x)
      strokeColor = "white"
      override def onClick(event: paperNs.MouseEvent): Unit | Boolean = mirrorsWithSameRow.foreach(_.click())
    }
  }
}
