package ui.paper

import typings.paperLib.paperMod.{^ => Paper}

class HoneycombFactory(radius: Int, maxRows: Int) {

  def create() = HoneyComb(radius, loop(firstRow, List(firstRow, zeroRow)).reverse)

  private val Length = 2 * Math.cos(Math.PI / 6) * radius

  private val Center = Point(Paper.view.center.x, Paper.view.center.x)

  private val zeroRow = Row(0, List(Sector(0, List(Center))))

  private val firstRow: Row = {
    val sectors = Center.hexagonVertices(Length, 0).zipWithIndex.map {
      case (point, i) => Sector(i + 1, List(point))
    }
    Row(1, sectors)
  }

  private def loop(row: Row, result: List[Row]): List[Row] = row.id match {
    case `maxRows` => result
    case _ =>
      val sectors = row.sectors.map { sector =>
        val points = sector.centers.flatMap { point =>
          List(sector.id - 1, sector.id).map { i =>
            point.shift(Length, i * Math.PI / 3)
          }
        }
        Sector(sector.id, points)
      }

      val newRow = Row(row.id + 1, sectors)
      loop(newRow, newRow :: result)
  }
}
