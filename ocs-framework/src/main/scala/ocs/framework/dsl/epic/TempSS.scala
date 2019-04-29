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
        println(s"current state=$Init, temp=$temp")
        temp := 45
        temp.pvPut()
        become(Ok)
      }
    case Ok =>
      when(temp > 40) {
        println(s"current state=$Ok, temp=$temp")
        temp := 25
        temp.pvPut()
        become(High)
      }
    case High =>
      when(temp < 30) {
        println(s"current state=$High, temp=$temp")
        temp.pvGet()
        become(Ok)
      }
  }

  override def debug(state: State): Unit = {
//    println(s"current state=$state, temp=$temp")
  }
}

object TempSS {
  sealed trait State

  case object Init extends State
  case object Ok   extends State
  case object High extends State
}
