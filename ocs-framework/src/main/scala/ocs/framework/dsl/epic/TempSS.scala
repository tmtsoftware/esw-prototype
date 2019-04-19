package ocs.framework.dsl.epic

import TempSS._
import ocs.framework.CswSystem
import ocs.framework.dsl.epic.internal._

class TempSS(cswSystem: CswSystem, externalService: ExternalService) extends Script2[State](Init, cswSystem) {

  val temp = Var(0).assign() // assign to remotePvVariable
//  externalService.subscribe("temperature updates").react(temp)

  override def machine(state: State): Unit = {
    println(s"current state=$state, temp=$temp")
    state match {
      case Init =>
        when(condition = true) {
          temp := 45
          become(Ok)
        }
      case Ok =>
        when(temp > 40) {
//          externalService.submit("decrease temperature", 30).react(temp)
          temp := 30
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
