package ocs.framework.dsl.epic

import ocs.framework.CswSystem

object Demo {

  sealed trait State

  case object Init extends State

  case object Ok extends State

  case object High extends State

  private val demoSystem = new CswSystem("demo")

  object A extends Script2[State](Init, demoSystem) {

    var temp = 0

    override def machine(state: State): Unit = state match {
      case Init =>
        when(condition = true) {
          temp = 45
          become(Ok)
        }
      case Ok =>
        when(temp > 40) {
          temp = 30
          become(High)
        }
      case High =>
        when(temp < 30) {
          become(Ok)
        }
    }
  }

}
