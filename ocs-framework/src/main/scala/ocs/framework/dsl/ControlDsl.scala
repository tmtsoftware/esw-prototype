package ocs.framework.dsl

import akka.Done
import csw.params.commands.CommandResponse
import ocs.api.models.AggregateResponse
import sequencer.macros.{AsyncMacros, StrandEc}

import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.concurrent.{ExecutionContext, Future}
import scala.language.experimental.macros

trait ControlDsl {
  implicit lazy val strandEc: StrandEc             = StrandEc.create()
  private implicit lazy val toEc: ExecutionContext = strandEc.ec
  private val loopInterval: FiniteDuration         = 50.millis

  protected def par(fs: Future[CommandResponse]*): Future[Set[CommandResponse]] = Future.sequence(fs.toSet)

  protected def parAggregate(fs: Future[AggregateResponse]*): Future[AggregateResponse] = spawn {
    val aggregateResponses = Future.sequence(fs.toSet).await
    aggregateResponses.foldLeft(AggregateResponse())(_ merge _)
  }

  protected implicit class RichF[T](t: Future[T]) {
    final def await: T = macro AsyncMacros.await
  }

  protected def spawn[T](body: => T)(implicit strandEc: StrandEc): Future[T] = macro AsyncMacros.asyncStrand[T]
  protected def loop(block: => Future[Boolean]): Future[Done] = loop(loopInterval)(block)

  protected def loop(minimumInterval: FiniteDuration)(block: => Future[Boolean]): Future[Done] =
    loopWithoutDelay(FutureUtils.delay(block, minimumInterval max loopInterval))

  private def loopWithoutDelay(block: => Future[Boolean]): Future[Done] = spawn {
    if (block.await) Done else loopWithoutDelay(block).await
  }

  protected def stopWhen(condition: Boolean): Boolean = condition

  def shutdown(): Future[Done] = spawn {
    onShutdown().await
    strandEc.shutdown()
    Done
  }

  protected def onShutdown(): Future[Done] = spawn(Done)
  def onStart(): Future[Done]              = spawn(Done)
  def onStop(): Future[Done]               = spawn(Done)
}
