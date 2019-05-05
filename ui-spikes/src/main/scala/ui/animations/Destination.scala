package ui.animations

import org.scalajs.dom.ext.Castable
import typings.stdLib.stdLibStrings.Event
import typings.svgDotJsLib.svgDotJsMod.{Element, Path}
import ui.animations.Diagram._

class Destination(elm: Element) {
  var selected = false

  def onClick(e: Event): Unit = {
    selected = true
    val x = elm.rbox().x
    val y = elm.rbox().y
    Light.animate(500).cast[Path].plot(Light.array().toString + s" L $x $y")
  }

  def animate(): Unit = {
    elm.click(onClick _)
  }
}
