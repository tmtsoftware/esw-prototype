package ocs.framework.dsl.epic.internal

import akka.stream.KillSwitch
import akka.stream.scaladsl.Sink
import MockEventService._
import scala.concurrent.Future

class ProcessVar[T](init: T, key: String)(implicit mc: Machine[_]) extends Var[T](init) {

  import mc.{ec, eventService, mat}

  def pvPut(): Future[Unit] =
    eventService.publish(key, MockEvent(key, get))

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
