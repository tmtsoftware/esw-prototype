package ui.mirror

class HoneycombFactory(centralHexagon: Hexagon, maxRows: Int) {

  def create() = HoneyComb(loop(firstRow, List(firstRow, zeroRow)).reverse)

  private val zeroRow = Row(0, List(Sector(0, List(centralHexagon))))

  private val firstRow: Row = {
    val sectors = centralHexagon.allNeighbours.zipWithIndex.map {
      case (hexagon, i) => Sector(i + 1, List(hexagon))
    }
    Row(1, sectors)
  }

  private def loop(row: Row, result: List[Row]): List[Row] = row.id match {
    case `maxRows` => result
    case _ =>
      val sectors = row.sectors.map { sector =>
        val hexagons = sector.hexagons.flatMap(_.neighbours(List(sector.id - 1, sector.id)))
        Sector(sector.id, hexagons)
      }

      val newRow = Row(row.id + 1, sectors)
      loop(newRow, newRow :: result)
  }
}
