package ocs.framework.dsl.epic

import TempSS._
import ocs.framework.CswSystem
import ocs.framework.dsl.epic.internal._
import ocs.framework.dsl.epic.internal.event.EpicsEvent

class TempSS(cswSystem: CswSystem, externalService: ExternalService) extends Machine[State](Init, cswSystem) {

  val temp: ProcessVar[Double] = Var.assign(0, "temperature updates", "temp")

  def logic: Logic = putGet

  def putMonitor: Logic = {
    case Init =>
      entry {
        temp.pvMonitor()
      }
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

  def putGet: Logic = {
    case Init =>
      when() {
        temp := 45
        temp.pvPut()
        become(Ok)
      }
    case Ok =>
      when(temp > 40) {
        temp := 25
        become(High)
      }
    case High =>
      when(temp < 30) {
        temp.pvGet()
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
