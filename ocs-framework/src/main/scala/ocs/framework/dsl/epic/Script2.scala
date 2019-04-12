package ocs.framework.dsl.epic

import akka.stream.scaladsl.{Sink, Source}
import ocs.framework.CswSystem
import sequencer.macros.StrandEc

import scala.concurrent.{ExecutionContext, Future}

abstract class Script2[State](init: State, cswSystem: CswSystem) {
  def machine(state: State): Unit

  private var currentState: State = init
  import cswSystem.materializer

  protected def become(state: State): Unit = {
    currentState = state
  }

  refresh()

  implicit lazy val strandEc: StrandEc           = StrandEc.create()
  private implicit lazy val ec: ExecutionContext = strandEc.ec

  private def refresh(): Future[Unit] = Future(machine(currentState))

  def when(condition: => Boolean)(body: => Unit): Unit = {
    if (condition) {
      body
      refresh()
    }
  }

  protected implicit class AssignableFuture[T](future: Future[T]) {
    def assign(updater: T => Unit): Future[Unit] = future.map(updater)
    def react(updater: T => Unit): Future[Unit]  = future.map(updater).flatMap(_ => refresh())
  }

  protected implicit class AssignableStream[T, Mat](source: Source[T, Mat]) {
    def assign(updater: T => Unit): Mat                          = runSequentially(x => Future(updater(x)))
    def react(updater: T => Unit): Mat                           = runSequentially(x => Future(updater(x)).flatMap(_ => refresh()))
    private def runSequentially(updater: T => Future[Unit]): Mat = source.mapAsync(1)(updater).to(Sink.ignore).run()
  }
}
