package ui.paper

import org.scalajs.dom.ext
import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperMod.{Color, Point, ^ => Paper}
import typings.paperLib.paperNs

import scala.scalajs.js.|
import scala.util.Random

class Honeycomb(radius: Int, rows: Int) {
  private val Length        = Math.cos(Math.PI / 6) * radius
  private val Center        = new Point(Paper.view.center)
  private val IndexedCenter = IndexedPoint(0, 0, Center)

  val initialFront: List[IndexedPoint] = (0 until 6).toList.map { sector =>
    val point = pointOff(Center, sector * 60, Length * 2)
    IndexedPoint(sector + 1, 1, point)
  }

  def loop(row: Int, front: List[IndexedPoint], result: List[IndexedPoint]): List[IndexedPoint] = row match {
    case `rows` => result
    case _ =>
      val newFront = front.flatMap { p =>
        List(p.sector - 1, p.sector)
          .map { i =>
            val point = pointOff(p.point, i * 60, Length * 2)
            IndexedPoint(p.sector, p.row + 1, point)
          }
      }

      loop(row + 1, newFront, result ::: newFront)
  }

  loop(1, initialFront, IndexedCenter :: initialFront).foreach(createHexagon)

  def createHexagon(point: IndexedPoint): RegularPolygon = new RegularPolygon(point.point, 6, radius) {
    fillColor = if (point.row <= 1) ext.Color.Black.toHex else ext.Color.all(point.sector).toHex
    if (point.row > 1) {
      strokeColor = "white"
    }
    override def onClick(event: paperNs.MouseEvent): Unit | Boolean = {
      val color = new Color(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
      fillColor = color
    }
  }

  def pointOff(center: Point, _angle: Double, _length: Double): Point = {
    val delta = new Point() {
      angle = _angle
      length = _length
    }
    new Point(center.add(delta))
  }
}

case class IndexedPoint(sector: Int, row: Int, point: Point) extends Proxy {
  override def self: Any = (point.x.round, point.y.round)
}
