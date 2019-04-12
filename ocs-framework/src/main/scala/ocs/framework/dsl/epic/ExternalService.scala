package ocs.framework.dsl.epic

import akka.actor.Cancellable
import akka.stream.scaladsl.Source
import ocs.framework.CswSystem

import scala.concurrent.Future
import scala.concurrent.duration.DurationLong

class ExternalService(cswSystem: CswSystem) {
  import cswSystem._

  def submit(command: String, targetTemperature: Int) = Future {
    Thread.sleep(2000)
    targetTemperature
  }

  def subscribe(command: String): Source[Int, Cancellable] = {
    var counter = 0
    Source.tick(5.seconds, 2.seconds, ()).map { _ =>
      counter += 10
      counter
    }
  }
}
