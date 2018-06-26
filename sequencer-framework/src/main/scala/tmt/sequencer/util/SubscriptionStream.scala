package tmt.sequencer.util

import akka.Done
import csw.services.event.scaladsl.EventSubscription
import org.tmt.macros.StrandEc
import tmt.sequencer.dsl.ScriptDsl

import scala.concurrent.Future

class SubscriptionStream(subscriptionStream: Future[EventSubscription])(implicit val strandEc: StrandEc) extends ScriptDsl {
  def cancel(): Future[Done] = spawn {
    subscriptionStream.await.unsubscribe().await
  }
}
