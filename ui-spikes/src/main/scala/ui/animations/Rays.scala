package ui.animations

import org.scalajs.dom.ext.Castable
import typings.stdLib.stdLibStrings.Event
import typings.svgDotJsLib.svgDotJsMod.{Element, Line, Path, Shape, ^ => SVG}

class Rays {
  JsMorph

  val mirror1: Line = SVG.get("mirror-1").cast[Line]
  val mirror2: Line = SVG.get("mirror-2").cast[Line]
  val mirror3: Line = SVG.get("mirror-3").cast[Line]

  val light: Path        = SVG.get("light").cast[Path]
  val intialPath: String = light.array().toString

  val shutter1: Element    = SVG.get("shutter-1")
  val topShutter1: Path    = SVG.get("top-shutter-1").cast[Path]
  val bottomShutter1: Path = SVG.get("bottom-shutter-1").cast[Path]

  val shutter2: Element    = SVG.get("shutter-2")
  val topShutter2: Path    = SVG.get("top-shutter-2").cast[Path]
  val bottomShutter2: Path = SVG.get("bottom-shutter-2").cast[Path]

  val irms: Element = SVG.get("irms")
  val iris: Element = SVG.get("iris")
  val wfs: Element  = SVG.get("wfs")

  val gates = List(
    new Gate(topShutter1, -25)(shutter1),
    new Gate(bottomShutter1, 25)(shutter1),
    new Gate(mirror1, 75)(),
    new Gate(mirror2, 75)(),
    new Gate(topShutter2, -25)(shutter2),
    new Gate(bottomShutter2, 25)(shutter2)
  )

  def animate(): Unit = {
    gates.foreach(_.animate())
    new Destination(irms).animate()
  }

  def blockingShape(): Shape = {
    gates.find(_.open == false).map(_.shape).getOrElse(mirror3)
  }

  class Gate(val shape: Shape, delta: Double)(related: Element = shape) {
    var nextY: Double = shape.y() + delta
    var open          = false

    def onClick(e: Event): Unit = {
      val currentY = shape.y()
      shape.animate().move(shape.x, nextY)
      nextY = currentY
      open = !open

      val targetX = blockingShape().rbox().cx - 15
      light.animate(500).cast[Path].plot(intialPath + s"H $targetX")
    }

    def animate(): Unit = {
      related.click(onClick _)
    }
  }

  class Destination(elm: Element) {
    var selected = false

    def onClick(e: Event): Unit = {
      selected = true
      val x = elm.rbox().x
      val y = elm.rbox().y
      light.animate(500).cast[Path].plot(light.array().toString + s" L $x $y")
    }

    def animate(): Unit = {
      elm.click(onClick _)
    }
  }
}
