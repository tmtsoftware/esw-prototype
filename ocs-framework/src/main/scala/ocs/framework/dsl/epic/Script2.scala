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

  run()

  implicit lazy val strandEc: StrandEc           = StrandEc.create()
  private implicit lazy val ec: ExecutionContext = strandEc.ec

  private def run(): Unit = strandEc.ec.execute(() => machine(currentState))

  def when(condition: => Boolean)(body: => Unit): Unit = {
    if (condition) {
      body
      run()
    }
  }

  protected implicit class AssignableFuture[T](future: Future[T]) {
    def assign(updater: T => Unit): Future[Unit] = future.map(updater)
    def react(updater: T => Unit): Future[Unit] = {
      assign {
        updater.andThen(_ => run())
      }
    }
  }

  protected implicit class AssignableStream[T, Mat](source: Source[T, Mat]) {
    def assign(updater: T => Unit): Mat = {
      source
        .mapAsync(1) { x =>
          Future(x).assign(updater)
        }
        .to(Sink.ignore)
        .run()
    }
    def react(updater: T => Unit): Mat = {
      assign {
        updater.andThen(_ => run())
      }
    }
  }

}
