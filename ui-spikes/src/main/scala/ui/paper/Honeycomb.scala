package ui.paper

import typings.paperLib.paperMod.{Point, ^ => Paper}

class Honeycomb(radius: Int, maxRows: Int) {
  private val Length = Math.cos(Math.PI / 6) * radius

  val initialRow: Row = {
    val Center = new Point(Paper.view.center)
    val sectors = (0 until 6).toList.map { sector =>
      val point = pointOff(Center, sector * 60, Length * 2)
      Sector(sector + 1, List(point))
    }
    Row(1, sectors)
  }

  val mirrors: List[Mirror] = Mirror.from(loop(initialRow, List(initialRow)).reverse.tail)

  def loop(row: Row, result: List[Row]): List[Row] = row.id match {
    case `maxRows` => result
    case _ =>
      val sectors = row.sectors.map { sector =>
        val points = sector.points.flatMap { point =>
          List(sector.id - 1, sector.id)
            .map { i =>
              pointOff(point, i * 60, Length * 2)
            }
        }
        Sector(sector.id, points)
      }

      val newRow = Row(row.id + 1, sectors)
      loop(newRow, newRow :: result)
  }

  def pointOff(center: Point, _angle: Double, _length: Double): Point = {
    val delta = new Point() {
      angle = _angle
      length = _length
    }
    new Point(center.add(delta))
  }
}

case class Mirror(sector: Int, row: Int, position: Int, point: Point) extends Proxy {
  override def self: Any          = (point.x.round, point.y.round)
  override def toString(): String = (sector, row, position, (point.x.round, point.y.round)).toString()
}

object Mirror {
  def from(rows: List[Row]): List[Mirror] = rows.flatMap { row =>
    row.sectors.flatMap { sector =>
      sector.points.zipWithIndex.map {
        case (point, position) => Mirror(sector.id, row.id, position, point)
      }.distinct
    }
  }
}

case class Sector(id: Int, points: List[Point])
case class Row(id: Int, sectors: List[Sector])
