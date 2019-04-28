package ui.mirror.svg

import typings.svgDotJsLib.svgDotJsMod.{Doc, Text}
import ui.mirror.{MyOwner, Store}

class SvgCounterComp(doc: Doc, store: Store) extends MyOwner {
  val text: Text = doc.text("").move(doc.width() / 2 - 50, 30)

  store.faultCounter.signal.foreach { count =>
    text.tspan(s"Fault Count = $count")
  }
}
