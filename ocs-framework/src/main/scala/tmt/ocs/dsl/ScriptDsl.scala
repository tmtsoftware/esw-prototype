package tmt.ocs.dsl

import akka.Done
import csw.params.commands.CommandResponse
import org.tmt.macros.{AsyncMacros, StrandEc}
import tmt.ocs.models.AggregateResponse

import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.concurrent.{ExecutionContext, Future}
import scala.language.experimental.macros

trait ScriptDsl {
  implicit def strandEc: StrandEc             = StrandEc.create()
  private implicit def toEc: ExecutionContext = strandEc.ec
  private val loopInterval: FiniteDuration    = 50.millis

  def par(fs: Future[CommandResponse]*): Future[Set[CommandResponse]] = Future.sequence(fs.toSet)

  def parAggregate(fs: Future[AggregateResponse]*): Future[AggregateResponse] = spawn {
    val aggregateResponses = Future.sequence(fs.toSet).await
    aggregateResponses.foldLeft(AggregateResponse.empty)(_ add _)
  }

  implicit class RichF[T](t: Future[T]) {
    final def await: T = macro AsyncMacros.await
  }

  def spawn[T](body: => T)(implicit strandEc: StrandEc): Future[T] = macro AsyncMacros.asyncStrand[T]
  def loop(block: => Future[Boolean]): Future[Done] = loop(loopInterval)(block)

  def loop(minimumInterval: FiniteDuration)(block: => Future[Boolean]): Future[Done] =
    loopWithoutDelay(FutureUtils.delay(block, minimumInterval max loopInterval))

  private def loopWithoutDelay(block: => Future[Boolean]): Future[Done] = spawn {
    if (block.await) Done else loopWithoutDelay(block).await
  }

  def stopWhen(condition: Boolean): Boolean = condition
}
