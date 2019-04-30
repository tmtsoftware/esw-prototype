package ocs.framework.dsl.epic

import TempSS._
import ocs.framework.CswSystem
import ocs.framework.dsl.epic.internal._

class TempSS(cswSystem: CswSystem, externalService: ExternalService) extends Machine[State](Init, cswSystem) {

  val temp: ProcessVar[Int] = Var.assign(0, "temperature updates")
  temp.monitor()

  def logic: Logic = {
    case Init =>
      when() {
        become(Ok)
      }
    case Ok =>
      entry {
        temp := 45
        temp.pvPut()
        temp := 35
      }
      when(temp > 40) {
        temp := 25
        become(High)
      }
    case High =>
      when(temp < 30) {
        become(Ok)
      }
  }

  override def debugString: String = s"temp = $temp"
}

object TempSS {
  sealed trait State

  case object Init extends State
  case object Ok   extends State
  case object High extends State
}
