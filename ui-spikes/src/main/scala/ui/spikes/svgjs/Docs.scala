package ui.spikes.svgjs

import typings.svgDotJsLib.svgDotJsMod.svgjsNs.{Doc, PointArray, Polygon, StrokeData}
import typings.svgDotJsLib.svgDotJsMod.{^ => SVG}

import scala.scalajs.js

object Docs {

  val draw: Doc = SVG("drawing").size(700, 700)

  def sketch(): Unit = {
    val pointArray       = ngon(10, 6)
    val polygon: Polygon = draw.polygon(Adapter(pointArray)).move(100, 100)
//    draw.polyline(pointArray).stroke(StrokeData("red", width = 2)).move(100, 100)
    polygon
    draw.line(Adapter(pointArray.take(2))).stroke(StrokeData("red", width = 2)).move(100, 100)
  }

  def ngon(radius: Double, edges: Int): PointArray = {
    val degrees = 360 / edges
    val dd = (0 until edges).map { i =>
      val a = i * degrees - 90
      val x = radius + radius * Math.cos(a * Math.PI / 180)
      val y = radius + radius * Math.sin(a * Math.PI / 180)
      Point(x, y)
    }
    PointArray(dd.toList)
  }

  def mgon(first: Point, second: Point, sides: Int) = {}
}
