package tmt.sequencer.util

import akka.actor.Cancellable
import org.tmt.macros.StrandEc
import tmt.sequencer.dsl.ScriptDsl

import scala.concurrent.Future

class PublisherStream(eventualCancellable: Future[Cancellable])(implicit strandEc: StrandEc) extends ScriptDsl {
  def cancel(): Future[Boolean] = spawn {
    eventualCancellable.await.cancel()
  }
}
