package tmt.ocs
import com.github.ahnfelt.react4s.Component
import tmt.ocs.sequencer.facade.NpmReactBridge

object Main {
  def main(arguments: Array[String]): Unit = {
    NpmReactBridge.renderToDomById(Component(MainComponent), "main")
  }
}
