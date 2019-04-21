package ocs.framework.dsl.epic.internal

import akka.stream.Materializer
import ocs.framework.CswSystem
import sequencer.macros.StrandEc

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

abstract class Machine[State](init: State, cswSystem: CswSystem) {
  def logic(state: State): Unit

  private var currentState: State = init

  implicit lazy val strandEc: StrandEc             = StrandEc.create()
  implicit lazy val ec: ExecutionContext           = strandEc.ec
  implicit lazy val mat: Materializer              = cswSystem.materializer
  implicit lazy val eventService: MockEventService = cswSystem.mockEventService
  implicit lazy val mach: Machine[State]           = this

  protected def become(state: State): Unit = {
    currentState = state
  }

  def refresh(): Future[Unit] = Future {
    logic(currentState)
  }

  def when(condition: => Boolean)(body: => Unit): Unit = {
    if (condition) {
      body
      refresh()
    }
  }

  implicit def varToT[T](reactive: Var[T]): T = reactive.get
}
