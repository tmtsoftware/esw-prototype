package ui.paper

import org.scalajs.dom.ext
import typings.paperLib.paperMod.PathNs.RegularPolygon
import typings.paperLib.paperMod.{Color, Point, ^ => Paper}
import typings.paperLib.paperNs

import scala.collection.{immutable, mutable}
import scala.scalajs.js.|
import scala.util.Random

class Honeycomb(radius: Int) {
  private val Length = Math.cos(Math.PI / 6) * radius
  private val Center = new Point(Paper.view.center)
  private val ci     = IndexedPoint(0, 0, Center)

  val initialFront: immutable.IndexedSeq[IndexedPoint] = (0 until 6).map { sector =>
    val point = pointOff(Center, sector * 60, Length * 2)
    IndexedPoint(sector + 1, 1, point)
  }

  var result: mutable.Set[IndexedPoint] = mutable.Set(initialFront: _*) + ci

  def loop(row: Int, front: Seq[IndexedPoint]): Unit = row match {
    case 3 =>
    case _ =>
      val newFront = front.flatMap { p =>
        val dd = (0 until 6)
          .map { i =>
            val point = pointOff(p.point, i * 60, Length * 2)
            IndexedPoint(p.sector, p.row + 1, point)
          }
          .filterNot(result)
          .take(p.row + 1)
        result = result ++ dd
        dd
      }

      loop(row + 1, newFront)
  }

  loop(1, initialFront)
  result.foreach(createHexagon)

  def createHexagon(point: IndexedPoint): RegularPolygon = new RegularPolygon(point.point, 6, radius) {
    fillColor = ext.Color.all(point.sector).toHex
    strokeColor = "black"
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
