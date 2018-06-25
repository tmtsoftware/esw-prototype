package tmt.sequencer.dsl

import org.tmt.macros.{AsyncMacros, StrandEc}
import tmt.sequencer.models.{AggregateResponse, CommandResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.experimental.macros

trait ScriptDsl {
  private implicit def toEc(implicit strandEc: StrandEc): ExecutionContext = strandEc.ec

  def par(fs: Future[CommandResponse]*)(implicit strandEc: StrandEc): Future[Set[CommandResponse]] = Future.sequence(fs.toSet)
  def parAggregate(fs: Future[AggregateResponse]*)(implicit strandEc: StrandEc): Future[AggregateResponse] = spawn {
    val aggregateResponses = Future.sequence(fs.toSet).await
    aggregateResponses.foldLeft(AggregateResponse: AggregateResponse)(_ add _)
  }

  implicit class RichF[T](t: Future[T]) {
    final def await: T = macro AsyncMacros.await
  }

  def spawn[T](body: => T)(implicit standEc: StrandEc): Future[T] = macro AsyncMacros.asyncStrand[T]
}
