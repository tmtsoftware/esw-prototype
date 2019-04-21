package ui.paper

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
    centers.distinct.map(point => Hexagon(id, rowId, 0, point))
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
    case (hexagon, i) => hexagon.copy(index = i)
  }
}

case class Hexagon(sector: Int, row: Int, index: Int, center: Point) {
  def position: Position = Position(sector, row, index)
}

case class Position(x: Int, y: Int, z: Int)
