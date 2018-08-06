package tmt

import org.scalajs.dom.raw.EventSource
import tmt.assembly.client.AssemblyCommandWebClient
import tmt.sequencer.api.SequenceResultsWeb
import tmt.sequencer.client.{ListComponentsClient, SequenceEditorWebClient, SequenceFeederWebClient}

object WebClients {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val gateway                      = new WebGateway()
  val feeder: SequenceFeederWebClient      = new SequenceFeederWebClient(gateway)
  val editor: SequenceEditorWebClient      = new SequenceEditorWebClient(gateway)
  val listSequencers: ListComponentsClient = new ListComponentsClient(gateway)
  val logger: EventSource                  = new EventSource(s"${SequenceResultsWeb.ApiName}/${SequenceResultsWeb.results}")

  val assemblyCommandClient: AssemblyCommandWebClient = new AssemblyCommandWebClient(gateway)
}
