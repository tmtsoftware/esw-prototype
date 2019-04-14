package ui.paper

import typings.paperLib.paperMod.{Path, Point}

import scala.scalajs.js

object Creator {

  def create(): Unit = {
    val path = new Path()
    path.strokeColor = "black"

    val start = new Point(100, 100)

    path.moveTo(start)
    path.lineTo(start.add(js.Array(200.0, -50)))
  }

}
