package ui.paper

class Display(store: Store) {

  def render(center: Point, radius: Int, maxRows: Int): Unit = {
    val centralHexagon = Hexagon(center, radius)
    val honeyComb      = new HoneycombFactory(centralHexagon, maxRows).create()
    val cells          = honeyComb.trimmedCells

    cells.foreach(cell => new Mirror(cell, store))
    println(cells.length)
    cells.take(30).foreach(println)
  }

}
