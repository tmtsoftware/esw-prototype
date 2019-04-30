package ocs.framework.dsl

import java.util.concurrent.ScheduledExecutorService

import sequencer.macros.StrandEc

import scala.async.Async._
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{Future, Promise}

object FutureUtils {
  def delay(duration: FiniteDuration, executorService: ScheduledExecutorService): Future[Unit] = {
    val p = Promise[Unit]()
    executorService.schedule(() => p.success(()), duration.length, duration.unit)
    p.future
  }

  def delay[T](minDelay: FiniteDuration)(future: => Future[T])(implicit strandEc: StrandEc): Future[T] = {
    async {
      val delayFuture = FutureUtils.delay(minDelay, strandEc.executorService)
      val futureValue = future
      await(delayFuture)
      await(futureValue)
    }(strandEc.ec)
  }
}
