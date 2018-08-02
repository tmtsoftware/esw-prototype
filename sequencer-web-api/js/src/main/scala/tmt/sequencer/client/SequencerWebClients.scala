package tmt.sequencer.client

import org.scalajs.dom.raw.EventSource
import tmt.sequencer.WebGateway
import tmt.sequencer.api.SequenceResultsWeb

object SequencerWebClients {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val gateway                      = new WebGateway()
  val feeder: SequenceFeederWebClient      = new SequenceFeederWebClient(gateway)
  val editor: SequenceEditorWebClient      = new SequenceEditorWebClient(gateway)
  val listSequencers: ListSequencersClient = new ListSequencersClient(gateway)
  val logger: EventSource                  = new EventSource(s"${SequenceResultsWeb.ApiName}/${SequenceResultsWeb.results}")
}
