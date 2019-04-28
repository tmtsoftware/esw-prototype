package ui.spikes.p5js

import typings.p5Lib.p5Mod.{^ => P5}

object Sketches {
  def sketch(): P5 = P5Facade { p =>
    import p._

    setup = () => {
      createCanvas(640, 480)
    }

    draw = () => {
      if (mouseIsPressed) {
        fill(0)
      } else {
        fill(255)
      }
      ellipse(mouseX, mouseY, 80, 80)
    }
  }
}
