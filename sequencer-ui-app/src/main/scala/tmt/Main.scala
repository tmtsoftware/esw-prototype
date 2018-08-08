package tmt
import com.github.ahnfelt.react4s.Component
import tmt.sequencer.facade.NpmReactBridge

object Main {
  def main(arguments: Array[String]): Unit = {
    NpmReactBridge.renderToDomById(Component(MainComponent), "main")
  }
}
