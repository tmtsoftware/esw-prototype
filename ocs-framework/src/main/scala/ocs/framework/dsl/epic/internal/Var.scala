package ocs.framework.dsl.epic.internal

import akka.stream.KillSwitch
import akka.stream.scaladsl.Sink
import ocs.framework.dsl.epic.internal.event.EpicsEvent

import scala.concurrent.Future

class Var[T](init: T, key: String, field: String)(implicit mc: Machine[_]) {
  @volatile
  private var _value = init
  def set(x: T): Unit = {
    _value = x
  }

  def :=(x: T): Unit = set(x)
  def get: T         = _value

  import mc.{ec, eventService, mat}

  def pvPut(): Unit = {
    eventService.publish(EpicsEvent(key, Map(field -> get)))
  }

  def pvGet(): Unit = {
    eventService.get(key).foreach(event => setValue(event, "pvGet"))
  }

  def pvMonitor(): KillSwitch = {
    eventService
      .subscribe(key)
      .mapAsync(1) { event =>
        Future.unit.flatMap { _ =>
          setValue(event, "monitor")
        }
      }
      .to(Sink.ignore)
      .run()
  }

  private def setValue(event: EpicsEvent, source: String) = {
    val value = event.params.getOrElse(field, init).asInstanceOf[T]
    set(value)
    mc.refresh("monitor")
  }

  override def toString: String = _value.toString
}

object Var {
  def assign[T](init: T, eventKey: String, processVar: String)(implicit machine: Machine[_]): Var[T] =
    new Var[T](init, eventKey, processVar)
}
