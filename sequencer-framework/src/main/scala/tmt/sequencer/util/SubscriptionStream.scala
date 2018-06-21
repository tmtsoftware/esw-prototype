package tmt.sequencer.util

import akka.Done
import csw.services.event.scaladsl.EventSubscription

import scala.concurrent.{ExecutionContext, Future}

case class SubscriptionStream(subscriptionStream: Future[EventSubscription])(implicit ec: ExecutionContext) {
  def cancel(): Future[Done] = subscriptionStream.flatMap(_.unsubscribe())
}
