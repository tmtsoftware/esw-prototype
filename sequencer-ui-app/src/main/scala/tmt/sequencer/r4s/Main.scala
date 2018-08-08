package tmt.sequencer.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.MainComponent
import tmt.sequencer.r4s.facade.NpmReactBridge

object Main {
  def main(arguments: Array[String]): Unit = {
    NpmReactBridge.renderToDomById(Component(MainComponent), "main")
  }
}
