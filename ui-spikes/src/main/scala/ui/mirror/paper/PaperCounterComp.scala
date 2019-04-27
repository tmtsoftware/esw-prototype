package ui.mirror.paper

import typings.paperLib.paperMod.{Point, PointText, ^ => Paper}
import ui.mirror.{MyOwner, Store}

class PaperCounterComp(store: Store) extends MyOwner {

  new PointText(new Point(Paper.view.center.x - 50, 30)) {
    store.faultCounter.signal.foreach { count =>
      content = s"Fault Count = $count"
    }
  }

}
