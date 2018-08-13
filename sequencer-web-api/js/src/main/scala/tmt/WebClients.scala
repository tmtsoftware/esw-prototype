package tmt

import org.scalajs.dom.raw.EventSource
import tmt.assembly.client.AssemblyCommandWebClient
import tmt.sequencer.api.SequenceResultsWeb
import tmt.sequencer.client.{ListComponentsClient, SequenceEditorWebClient, SequenceFeederWebClient}
import tmt.sequencer.models.SequencerInfo

object WebClients {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val gatewayHost = "http://localhost:9090"

  lazy val listSequencers: ListComponentsClient = new ListComponentsClient(new WebGateway(gatewayHost))

  def feeder(sequencerInfo: SequencerInfo): SequenceFeederWebClient = new SequenceFeederWebClient(sequencerClient(sequencerInfo))
  lazy val editor: SequenceEditorWebClient                          = new SequenceEditorWebClient(new WebGateway(gatewayHost))
  lazy val logger: EventSource                                      = new EventSource(s"$gatewayHost/${Path.hashPath}${SequenceResultsWeb.results}")

  lazy val assemblyCommandClient: AssemblyCommandWebClient = new AssemblyCommandWebClient(new WebGateway(gatewayHost))

  private def sequencerClient(sequencerInfo: SequencerInfo) =
    new WebGateway(s"$gatewayHost/sequencer/${sequencerInfo.id}/${sequencerInfo.mode}")
}
