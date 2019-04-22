package ocs.framework.dsl.epic

import TempSS._
import ocs.framework.CswSystem
import ocs.framework.dsl.epic.internal._

class TempSS(cswSystem: CswSystem, externalService: ExternalService) extends Machine[State](Init, cswSystem) {

  val temp: ProcessVar[Int] = Var.assign(0, "temperature updates")
  temp.monitor()

  def logic: Logic = {
    case Init =>
      when(condition = true) {
        temp := 45
        temp.pvPut()
        become(Ok)
      }
    case Ok =>
      when(temp > 40) {
//          externalService.submit("decrease temperature", 30).react(temp)
        temp := 25
        temp.pvPut()
        become(High)
      }
    case High =>
      when(temp < 30) {
        temp.pvGet()
        become(Ok)
      }
  }

  override def debug(state: State): Unit = {
    println(s"current state=$state, temp=$temp")
  }
}

object TempSS {
  sealed trait State

  case object Init extends State
  case object Ok   extends State
  case object High extends State
}
