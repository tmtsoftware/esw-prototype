package ui.paper

import com.raquo.airstream.ownership.Owner
import com.raquo.airstream.signal.{Signal, Var}
import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperNs

import scala.scalajs.js.|

class Display(radius: Int, maxRows: Int) {
  lazy val hexagons: List[Hexagon]                  = new HoneycombFactory(radius, maxRows).create().trimmedHexagons
  lazy val mirrors: List[Mirror]                    = hexagons.map(hexagon => new Mirror(hexagon, radius))
  lazy val displaysBySector: Map[Int, List[Mirror]] = mirrors.groupBy(_.hexagon.sector)
  lazy val displaysByRow: Map[Int, List[Mirror]]    = mirrors.groupBy(_.hexagon.row)

  def honeyComb(): Unit = {
    mirrors
    println(hexagons.length)
    hexagons.foreach(println)
  }

  val Colors = List("#E7CFA0", "#7CC1D2", "#A97FFF")

  class Mirror(val hexagon: Hexagon, radius: Int) extends MyOwner {
    def flip(): Unit = clicked.set(!clicked.now())

    private val clicked = Var(false)

    val color: Signal[String] = clicked.signal.map {
      case false => List("#E7CFA0", "#7CC1D2", "#A97FFF")(hexagon.sector % 3)
      case true  => "red"
    }

    private lazy val mirrorsWithSameRow = displaysByRow.getOrElse(hexagon.row, Nil)

    new RegularPolygon(hexagon.point, 6, radius) {
      color.foreach(x => fillColor = x)
      strokeColor = "white"
      override def onClick(event: paperNs.MouseEvent): Unit | Boolean = mirrorsWithSameRow.foreach(_.flip())
    }
  }
}

trait MyOwner {
  implicit val owner: Owner = new Owner {}
}
