package tmt.sequencer

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object FutureExt {
  implicit class RichFuture[T](val f: Future[T]) extends AnyVal {
    def get: T = Await.result(f, Duration.Inf)
  }
}
