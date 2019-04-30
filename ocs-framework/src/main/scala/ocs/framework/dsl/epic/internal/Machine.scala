package ocs.framework.dsl.epic.internal

import akka.stream.Materializer
import ocs.framework.CswSystem
import ocs.framework.dsl.epic.internal.event.EpicsEventService
import sequencer.macros.StrandEc

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

abstract class Machine[State](init: State, cswSystem: CswSystem) {
  type Logic = State => Unit

  def logic: Logic

  private var currentState: State  = init
  private var previousState: State = _

  implicit lazy val strandEc: StrandEc              = StrandEc.create()
  implicit lazy val ec: ExecutionContext            = strandEc.ec
  implicit lazy val mat: Materializer               = cswSystem.materializer
  implicit lazy val eventService: EpicsEventService = cswSystem.mockEventService
  implicit lazy val mach: Machine[State]            = this

  protected def become(state: State): Unit = {
    currentState = state
  }

  def refresh(source: String): Future[Unit] = {
    Future {
      println(
        f"previousState = $previousState%-8s     currentState = $currentState%-8s    action = $source%-8s     $debugString%8s"
      )
      logic(currentState)
    }
  }

  def when(condition: => Boolean = true)(body: => Unit): Unit = {
    previousState = currentState
    if (condition) {
      body
      refresh("when")
    }
  }

  def entry(body: => Unit): Unit = {
    if (currentState != previousState) {
      body
    }
  }

  def debugString: String = ""

  implicit def varToT[T](reactive: Var[T]): T = reactive.get
}

object Machine {
  type Logic[T] = T => Unit
}
