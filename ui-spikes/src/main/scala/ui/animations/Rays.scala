package ui.animations

import org.scalajs.dom.ext.Castable
import typings.stdLib.stdLibStrings.Event
import typings.svgDotJsLib.svgDotJsMod.{Line, Path, ^ => SVG}

object Rays {

  def run(): Unit = {
    JsMorph

    val mirror1 = SVG.get("mirror-1").cast[Line]
    var nextY   = mirror1.y() + 75

    val light    = SVG.get("light").cast[Path]
    var nextPath = light.array().toString + "H 283"

    mirror1.click { _: Event =>
      val currentY = mirror1.y()
      mirror1.animate().move(mirror1.x, nextY)
      nextY = currentY

      val currentPath = light.array().toString
      light.animate().cast[Path].plot(nextPath)
      nextPath = currentPath
    }
  }

}
