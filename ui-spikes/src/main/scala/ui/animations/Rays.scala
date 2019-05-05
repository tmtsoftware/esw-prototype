package ui.animations

import ui.animations.Diagram._

class Rays {
  JsMorph
  def animate(): Unit = {
    Gates.gates.foreach(_.animate())
    new Destination(Irms).animate()
  }
}
