package ocs.framework.dsl.epic.internal

import akka.stream.KillSwitch
import akka.stream.scaladsl.Sink

import scala.concurrent.Future
import scala.language.implicitConversions

class Var[T](init: T) {
  @volatile
  private var _value = init
  def set(x: T): Unit = {
    _value = x
  }

  def :=(x: T): Unit = set(x)
  def get: T         = _value

  override def toString: String = _value.toString
}

class ProcessVar[T](init: T, key: String)(implicit mc: Machine[_]) extends Var[T](init) {

  import mc.{ec, eventService, mat}

  def pvPut(): Future[Unit] = Future.unit.flatMap { _ =>
    eventService.publish(key, MockEvent(key, get))
  }

  def pvGet(): Future[Unit] = {
    eventService.get(key).map { option =>
      option.foreach { event =>
        set(event.value.asInstanceOf[T])
        mc.refresh()
      }
    }
  }

  def monitor(): KillSwitch = {
    eventService
      .subscribe(key)
      .mapAsync(1) { event =>
        Future {
          set(event.value.asInstanceOf[T])
          mc.refresh()
        }
      }
      .to(Sink.ignore)
      .run()
  }
}

object Var {
  def apply[T](init: T): Var[T]                                                    = new Var(init)
  def assign[T](init: T, key: String)(implicit machine: Machine[_]): ProcessVar[T] = new ProcessVar[T](init, key)
}
