package ocs.framework.dsl.epic

import TempSS._
import ocs.framework.CswSystem

class TempSS(cswSystem: CswSystem, externalService: ExternalService) extends Script2[State](Init, cswSystem) {

  var temp = 0 // assign to remotePvVariable
  externalService.subscribe("temperature updates").react(x => temp = x)

  override def machine(state: State): Unit = {
    println(s"current state=$state, temp=$temp")
    state match {
      case Init =>
        when(condition = true) {
          temp = 45
          become(Ok)
        }
      case Ok =>
        when(temp > 40) {
          externalService.submit("decrease temperature", 30).react(x => temp = x)
          become(High)
        }
      case High =>
        when(temp < 30) {
          become(Ok)
        }
    }
  }
}

object TempSS {
  sealed trait State

  case object Init extends State
  case object Ok   extends State
  case object High extends State
}
