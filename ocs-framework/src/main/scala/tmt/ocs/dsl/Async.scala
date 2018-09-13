package tmt.ocs.dsl

import scala.annotation.compileTimeOnly
import scala.async.internal
import scala.concurrent.{ExecutionContext, Future}
import scala.language.experimental.macros

object Async {
  def async[T](body: => T)(implicit execContext: ExecutionContext): Future[T] =
    macro internal.ScalaConcurrentAsync.asyncImpl[T]

  @compileTimeOnly("`await` must be enclosed in an `spawn` block")
  def await[T](awaitable: Future[T]): T = ???
}
