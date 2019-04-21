package ui.mirror

case class HoneyComb(rows: List[Row]) {
  def cells: List[Cell]        = rows.flatMap(_.cells)
  def trimmedCells: List[Cell] = rows.drop(2).flatMap(_.trimmedCells(rows.length - 1))
}

case class Row(id: Int, sectors: List[Sector]) {
  def cells: List[Cell]                      = sectors.flatMap(_.cells(id))
  def trimmedCells(maxRows: Int): List[Cell] = sectors.flatMap(_.trimmedCells(id, maxRows))
}

case class Sector(id: Int, hexagons: List[Hexagon]) {
  def cells(rowId: Int): List[Cell] = reposition {
    hexagons.distinct.map(hexagon => Cell(id, rowId, 0, hexagon))
  }

  def trimmedCells(rowId: Int, maxRows: Int): List[Cell] = reposition {
    val _cells = cells(rowId)
    if (rowId == maxRows) {
      val takeCount = maxRows / 2
      val dropCount = (maxRows - takeCount + 1) / 2
      _cells.slice(dropCount, dropCount + takeCount)
    } else if (rowId == maxRows - 1) {
      _cells.drop(1)
    } else {
      _cells
    }
  }

  private def reposition(cells: List[Cell]): List[Cell] = cells.zipWithIndex.map {
    case (cell, i) => cell.copy(index = i)
  }
}

case class Cell(sector: Int, row: Int, index: Int, hexagon: Hexagon) {
  def position: Position = Position(sector, row, index)

  override def toString: String = s"row=$row sector=$sector index=$index $hexagon"
}

case class Position(x: Int, y: Int, z: Int)
