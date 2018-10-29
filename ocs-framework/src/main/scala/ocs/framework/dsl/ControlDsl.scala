package ocs.framework.dsl

import akka.Done
import csw.params.commands.CommandResponse
import sequencer.macros.{AsyncMacros, StrandEc}

import scala.concurrent.duration.{DurationDouble, FiniteDuration}
import scala.concurrent.{ExecutionContext, Future}
import scala.language.experimental.macros

trait ControlDsl {
  implicit lazy val strandEc: StrandEc             = StrandEc.create()
  private implicit lazy val toEc: ExecutionContext = strandEc.ec
  private val loopInterval: FiniteDuration         = 50.millis

  //It should return list
  protected def par(fs: Future[CommandResponse]*): Future[List[CommandResponse]] = Future.sequence(fs.toList)

  /*TODO: 1. SubmitResponse does not have empty constructor
  2. usage of parAggregate
       parAggregate(
      iris.await.submit(irisSequence).await
      tcs.await.submit(tcsSequence).await
      ).await
  In this case script writer should know logic of inferring final SubmitResponse depending on responses of iris and tcs
  So as per new way par and parAggregate both should return List[SubmitResponse]. Then script writer should update that
  in crm or infer on own

  protected def parAggregate(fs: Future[SubmitResponse]*): Future[SubmitResponse] = spawn {
    val submitResponses = Future.sequence(fs.toSet).await
    submitResponses.foldLeft(SubmitResponse)(_ merge _)
  }
   */

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
  def abort(): Future[Done]                = spawn(Done)
}
