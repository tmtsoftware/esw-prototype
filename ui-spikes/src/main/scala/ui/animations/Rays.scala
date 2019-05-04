package ui.animations

import org.scalajs.dom.ext.Castable
import typings.stdLib.stdLibStrings.Event
import typings.svgDotJsLib.svgDotJsMod.{Line, Path, Shape, ^ => SVG}

class Rays {
  JsMorph

  val mirror1: Line = SVG.get("mirror-1").cast[Line]
  val mirror2: Line = SVG.get("mirror-2").cast[Line]
  val mirror3: Line = SVG.get("mirror-3").cast[Line]

  val light: Path        = SVG.get("light").cast[Path]
  val intialPath: String = light.array().toString

  val topShutter1: Path    = SVG.get("top-shutter-1").cast[Path]
  val bottomShutter1: Path = SVG.get("bottom-shutter-1").cast[Path]

  val topShutter2: Path    = SVG.get("top-shutter-2").cast[Path]
  val bottomShutter2: Path = SVG.get("bottom-shutter-2").cast[Path]

  def animate(topShutter: Path, bottomShutter: Path): Unit = {}

  val gates = List(
    new Gate(topShutter1, -25, bottomShutter1),
    new Gate(bottomShutter1, 25, topShutter1),
    new Gate(mirror1, 75),
    new Gate(mirror2, 75),
    new Gate(topShutter2, -25, bottomShutter2),
    new Gate(bottomShutter2, 25, topShutter2)
  )

  def animate(): Unit = {
    gates.foreach(_.animate())
  }

  def blockingShape(): Shape = {
    gates.find(_.open == false).map(_.shape).getOrElse(mirror3)
  }

  class Gate(val shape: Shape, delta: Double, related: Shape*) {
    val light: Path = SVG.get("light").cast[Path]

    var nextY: Double = shape.y() + delta
    var open          = false

    def onClick(e: Event): Unit = {
      val currentY = shape.y()
      shape.animate().move(shape.x, nextY)
      nextY = currentY
      open = !open

      val targetX = blockingShape().rbox().cx - 5
      light.animate().cast[Path].plot(intialPath + s"H $targetX")
    }

    def animate(): Unit = {
      shape.click(onClick _)
      related.foreach(_.click(onClick _))
    }
  }
}
