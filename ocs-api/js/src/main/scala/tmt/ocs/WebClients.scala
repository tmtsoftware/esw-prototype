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

  def feeder(sequencerInfo: SequencerInfo): SequenceFeederJsClient = new SequenceFeederJsClient(makeGateway(sequencerInfo))
  def editor(sequencerInfo: SequencerInfo): SequenceEditorJsClient = new SequenceEditorJsClient(makeGateway(sequencerInfo))
  def results(sequencerInfo: SequencerInfo): EventSource           = makeGateway(sequencerInfo).source(SequenceResultsWeb.results)

  private def makeGateway(sequencerInfo: SequencerInfo) =
    new WebGateway(s"$gatewayHost/sequencer/${sequencerInfo.id}/${sequencerInfo.mode}/")

}
