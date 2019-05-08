package ocs.framework.dsl.epic

import TemperatureMonitor._
import ocs.framework.CswSystem
import ocs.framework.dsl.epic.internal._

import scala.concurrent.duration.DurationLong

class TemperatureMonitor(cswSystem: CswSystem) extends Machine[State](Init, cswSystem) {

  val temp: Var[Int] = Var.assign(0, "nfiraos.temp.updates", "temp")

  def logic: Logic = putGet

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

  def putMonitor: Logic = {
    case Init =>
      entry {
        temp.pvMonitor()
      }

      when(delay = 1.seconds) {
        become(Ok)
      }
    case Ok =>
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

object TemperatureMonitor {
  sealed trait State

  case object Init extends State
  case object Ok   extends State
  case object High extends State
}
