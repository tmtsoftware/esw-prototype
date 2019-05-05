package ui.animations

import org.scalajs.dom.ext.Castable
import typings.stdLib.stdLibStrings.Event
import typings.svgDotJsLib.svgDotJsMod.{Element, Path, Shape}
import ui.animations.Diagram._

object Gates {
  val intialPath: String = Light.array().toString

  val gates = List(
    new Gate(TopShutter1, -25)(Shutter1),
    new Gate(BottomShutter1, 25)(Shutter1),
    new Gate(Mirror1, 75)(),
    new Gate(Mirror2, 75)(),
    new Gate(TopShutter2, -25)(Shutter2),
    new Gate(BottomShutter2, 25)(Shutter2)
  )

  def blockingShape(): Shape = {
    gates.find(_.open == false).map(_.shape).getOrElse(Mirror3)
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
      Light.animate(500).cast[Path].plot(intialPath + s"H $targetX")
    }

    def animate(): Unit = {
      related.click(onClick _)
    }
  }
}
