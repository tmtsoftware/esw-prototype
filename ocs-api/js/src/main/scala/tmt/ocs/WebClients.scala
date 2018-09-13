package tmt.ocs

import org.scalajs.dom.raw.EventSource
import tmt.ocs.api.SequenceResultsWeb
import tmt.ocs.client.{AssemblyFeederJsClient, ListComponentsJsClient, SequenceEditorJsClient, SequenceFeederJsClient}
import tmt.ocs.models.SequencerInfo

object WebClients {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val gatewayHost = "http://localhost:9090"

  lazy val listSequencers: ListComponentsJsClient = new ListComponentsJsClient(new WebGateway(gatewayHost))

  def assemblyCommandClient(assemblyName: String): AssemblyFeederJsClient =
    new AssemblyFeederJsClient(new WebGateway(s"$gatewayHost/assembly/$assemblyName/"))

  def feeder(sequencerInfo: SequencerInfo): SequenceFeederJsClient = new SequenceFeederJsClient(sequencerClient(sequencerInfo))
  def editor(sequencerInfo: SequencerInfo): SequenceEditorJsClient = new SequenceEditorJsClient(sequencerClient(sequencerInfo))
  def results(sequencerInfo: SequencerInfo): EventSource =
    new EventSource(s"${sequencerPath(sequencerInfo)}${SequenceResultsWeb.results}")

  private def sequencerClient(sequencerInfo: SequencerInfo) = new WebGateway(sequencerPath(sequencerInfo))

  private def sequencerPath(sequencerInfo: SequencerInfo) = s"$gatewayHost/sequencer/${sequencerInfo.id}/${sequencerInfo.mode}/"
}
