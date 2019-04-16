package ui.paper

import typings.paperLib.paperMod.{Point, ^ => Paper}

class HoneycombFactory(radius: Int, maxRows: Int) {

  def create() = HoneyComb(radius, loop(firstRow, List(firstRow, zeroRow)).reverse)

  private val Length = Math.cos(Math.PI / 6) * radius

  private val Center = new Point(Paper.view.center)

  private val zeroRow = Row(0, List(Sector(0, List(Center))))

  private val firstRow: Row = {
    val sectors = (0 until 6).toList.map { sector =>
      val point = shift(Center, sector * 60, Length * 2)
      Sector(sector + 1, List(point))
    }
    Row(1, sectors)
  }

  private def loop(row: Row, result: List[Row]): List[Row] = row.id match {
    case `maxRows` => result
    case _ =>
      val sectors = row.sectors.map { sector =>
        val points = sector.centers.flatMap { point =>
          List(sector.id - 1, sector.id).map { i =>
            shift(point, i * 60, Length * 2)
          }
        }
        Sector(sector.id, points)
      }

      val newRow = Row(row.id + 1, sectors)
      loop(newRow, newRow :: result)
  }

  private def shift(center: Point, _angle: Double, _length: Double): Point = {
    val delta = new Point() {
      angle = _angle
      length = _length
    }
    new Point(center.add(delta))
  }
}
