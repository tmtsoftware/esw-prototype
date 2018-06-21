package tmt.sequencer.util

import akka.actor.Cancellable

import scala.concurrent.{ExecutionContext, Future}

case class PublisherStream(eventualCancellable: Future[Cancellable])(implicit ec: ExecutionContext) {
  def cancel(): Future[Boolean] = eventualCancellable.map(_.cancel())
}
