package ui

import ui.p5js.P5Facade
import typings.stdLib.^.console
import typings.svgDotJsLib.svgDotJsMod.{^ => SVG}

object Main {

  def main(arguments: Array[String]): Unit = {
    val draw = SVG("drawing").size(300, 300)
    val rect = draw
      .rect(100, 100)
      .attr("fill", "#f06")
      .attr("aaaaaaaaa", 23)

    println((rect.x(), rect.y(), rect.cx(), rect.cy()))

    val sketch = P5Facade { p =>
      val x = 100
      val y = 100

      p.setup = () => p.createCanvas(700, 410)

      p.draw = () => {
        p.background(0)
        p.fill(255)
        p.rect(x, y, 50, 50)
      }
    }
    console.warn(sketch.windowHeight)
  }

}
