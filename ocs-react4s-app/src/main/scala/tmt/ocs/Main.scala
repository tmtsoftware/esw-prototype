package tmt.ocs

import com.github.ahnfelt.react4s.Component
import tmt.r4s.facade.NpmReactBridge

object Main {
  def main(arguments: Array[String]): Unit = {
    NpmReactBridge.renderToDomById(Component(MainComponent), "main")
  }
}
