package tmt.sequencer.dsl

import java.util.concurrent.Executors

import akka.Done
import org.tmt.macros.AsyncMacros
import tmt.sequencer.models.{AggregateResponse, CommandResponse}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}
import scala.language.experimental.macros

trait ActiveObject {
  protected implicit val ec: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newSingleThreadExecutor())

  def par(fs: Future[CommandResponse]*): Future[Set[CommandResponse]] = Future.sequence(fs.toSet)
  def parAggregate(fs: Future[AggregateResponse]*): Future[AggregateResponse] = spawn {
    val aggregateResponses = Future.sequence(fs.toSet).await
    aggregateResponses.foldLeft(AggregateResponse: AggregateResponse)(_ add _)
  }

  implicit class RichF[T](t: Future[T]) {
    final def await: T = macro AsyncMacros.await
  }

  def spawn[T](body: => T)(implicit ec: ExecutionContext): Future[T] = macro AsyncMacros.async[T]

  private[sequencer] def shutdownEc(): Done = {
    ec.shutdown()
    Done
  }
}
