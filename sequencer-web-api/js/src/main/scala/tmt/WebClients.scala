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

  def assemblyCommandClient(assemblyName: String): AssemblyCommandWebClient =
    new AssemblyCommandWebClient(new WebGateway(s"$gatewayHost/assembly/$assemblyName/"))

  def feeder(sequencerInfo: SequencerInfo): SequenceFeederWebClient = new SequenceFeederWebClient(sequencerClient(sequencerInfo))
  def editor(sequencerInfo: SequencerInfo): SequenceEditorWebClient = new SequenceEditorWebClient(sequencerClient(sequencerInfo))
  def results(sequencerInfo: SequencerInfo): EventSource =
    new EventSource(s"${sequencerPath(sequencerInfo)}${SequenceResultsWeb.results}")

  private def sequencerClient(sequencerInfo: SequencerInfo) = new WebGateway(sequencerPath(sequencerInfo))

  private def sequencerPath(sequencerInfo: SequencerInfo) = s"$gatewayHost/sequencer/${sequencerInfo.id}/${sequencerInfo.mode}/"
}
