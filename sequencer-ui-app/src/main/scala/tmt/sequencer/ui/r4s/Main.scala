package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.ui.r4s.facade.NpmReactBridge

object Main {
  def main(arguments: Array[String]): Unit = {
    val component = Component(MainComponent)
    NpmReactBridge.renderToDomById(component, "main")
  }
}
