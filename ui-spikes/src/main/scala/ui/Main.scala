package ui

import typings.stdLib.^.{console, window}
import typings.svgDotJsLib.svgDotJsMod.svgjsNs.Line
import ui.p5js.Sketches
import ui.paper.{Boot, Creator}
import ui.svgjs.Docs

object Main {

  def main(arguments: Array[String]): Unit = {
//    val rect = Docs.sketch()
//    Docs.ngon(10, 4).foreach(println)
//    println((rect.x(), rect.y(), rect.cx(), rect.cy()))

//    val sketch = Sketches.sketch()
//    console.warn(sketch.windowHeight)

    window.onload = Boot.start
  }

}
