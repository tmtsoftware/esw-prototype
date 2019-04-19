package ocs.framework.dsl.epic.internal

import akka.stream.{KillSwitch, Materializer}
import akka.stream.scaladsl.{Sink, Source}
import ocs.framework.CswSystem
import sequencer.macros.StrandEc

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

class Var[T](init: T) {
  private var _value = init
  def set(x: T): Unit = {
    _value = x
  }

  def :=(x: T): Unit = set(x)
  def get: T         = _value

  override def toString: String = _value.toString
}

class ProcessVar[T](init: T, key: String)(implicit mc: Script2[Any]) extends Var[T](init) {

  import mc.ec
  import mc.es
  import mc.mat

  def pvPut(): Future[Unit] = Future.unit.flatMap { _ =>
    es.publish(key, MockEvent(key, get))
  }

  def pvGet(): Future[Unit] = {
    es.get(key).map { x =>
      x.foreach { y =>
        set(y.value.asInstanceOf[T])
        mc.refresh()
      }
    }
  }

  def monitor(): KillSwitch = {
    es.subscribe(key)
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
  def apply[T](init: T): Var[T]                                               = new Var(init)
  def assign[T](init: T, key: String)(implicit script2: Script2[Any]): Var[T] = new ProcessVar[T](init, key)
}

abstract class Script2[State](init: State, cswSystem: CswSystem) {
  def machine(state: State): Unit

  private var currentState: State = init
//  import cswSystem.materializer

  implicit lazy val strandEc: StrandEc   = StrandEc.create()
  implicit lazy val ec: ExecutionContext = strandEc.ec
  implicit lazy val es: MockEventService = cswSystem.mockEventService
  implicit lazy val mat: Materializer    = cswSystem.materializer

  protected def become(state: State): Unit = {
    currentState = state
  }

  refresh()

  def refresh(): Future[Unit] = Future(machine(currentState))

  def when(condition: => Boolean)(body: => Unit): Unit = {
    if (condition) {
      body
      refresh()
    }
  }

  protected implicit class AssignableFuture[T](future: Future[T]) {
//    def assign(reactive: Var[T]): Future[Unit] = future.map(reactive.set)
//    def react(reactive: Var[T]): Future[Unit]  = future.map(reactive.set).flatMap(_ => refresh())
  }

  protected implicit class AssignableStream[T, Mat](source: Source[T, Mat]) {
//    def assign(reactive: Var[T]): Mat                            = runSequentially(x => Future(reactive.set(x)))
//    def react(reactive: Var[T]): Mat                             = runSequentially(x => Future(reactive.set(x)).flatMap(_ => refresh()))
//    private def runSequentially(updater: T => Future[Unit]): Mat = source.mapAsync(1)(updater).to(Sink.ignore).run()
  }

  implicit def varToT[T](reactive: Var[T]): T = reactive.get
}
