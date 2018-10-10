package ocs.react4s.app

import com.github.ahnfelt.react4s.Component
import react4s.facade.NpmReactBridge

object Main {
  def main(arguments: Array[String]): Unit = {
    NpmReactBridge.renderToDomById(Component(MainComponent), "main")
  }
}
