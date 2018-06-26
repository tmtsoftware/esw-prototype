package tmt.sequencer.dsl

import akka.Done
import csw.messages.commands.CommandResponse
import org.tmt.macros.{AsyncMacros, StrandEc}
import tmt.sequencer.models.AggregateResponse

import scala.concurrent.{ExecutionContext, Future}
import scala.language.experimental.macros

trait ScriptDsl {
  implicit def strandEc: StrandEc
  private implicit def toEc: ExecutionContext = strandEc.ec

  def par(fs: Future[CommandResponse]*): Future[Set[CommandResponse]] = Future.sequence(fs.toSet)
  def parAggregate(fs: Future[AggregateResponse]*): Future[AggregateResponse] = spawn {
    val aggregateResponses = Future.sequence(fs.toSet).await
    aggregateResponses.foldLeft(AggregateResponse: AggregateResponse)(_ add _)
  }

  implicit class RichF[T](t: Future[T]) {
    final def await: T = macro AsyncMacros.await
  }

  def spawn[T](body: => T)(implicit strandEc: StrandEc): Future[T] = macro AsyncMacros.asyncStrand[T]

  def loop(block: => Future[Boolean]): Future[Done] = spawn {
    if (block.await) Done else loop(block).await
  }

  def stopWhen(condition: Boolean): Boolean = condition
}
