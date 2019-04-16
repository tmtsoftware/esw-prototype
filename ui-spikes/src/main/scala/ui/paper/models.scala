package ui.paper

import typings.paperLib.paperMod.Point

case class HoneyComb(radius: Int, rows: List[Row]) {
  def hexagons: List[Hexagon]        = rows.flatMap(_.hexagons)
  def trimmedHexagons: List[Hexagon] = rows.drop(2).flatMap(_.trimmedHexagons(rows.length - 1))
}

case class Row(id: Int, sectors: List[Sector]) {
  def hexagons: List[Hexagon]                      = sectors.flatMap(_.hexagons(id))
  def trimmedHexagons(maxRows: Int): List[Hexagon] = sectors.flatMap(_.trimmedHexagons(id, maxRows))
}

case class Sector(id: Int, centers: List[Point]) {
  def hexagons(rowId: Int): List[Hexagon] = reposition {
    centers.map(point => Hexagon(id, rowId, 0, point)).distinct
  }

  def trimmedHexagons(rowId: Int, maxRows: Int): List[Hexagon] = reposition {
    val _hexagons = hexagons(rowId)
    if (rowId == maxRows) {
      val takeCount = maxRows / 2
      val dropCount = (maxRows - takeCount + 1) / 2
      _hexagons.slice(dropCount, dropCount + takeCount)
    } else if (rowId == maxRows - 1) {
      _hexagons.drop(1)
    } else {
      _hexagons
    }
  }

  private def reposition(hexagons: List[Hexagon]): List[Hexagon] = hexagons.zipWithIndex.map {
    case (hexagon, i) => hexagon.copy(position = i)
  }
}

case class Hexagon(sector: Int, row: Int, position: Int, point: Point) extends Proxy {
  override def self: Any          = (point.x.round, point.y.round)
  override def toString(): String = (row, sector, position, (point.x.round, point.y.round)).toString()
}
