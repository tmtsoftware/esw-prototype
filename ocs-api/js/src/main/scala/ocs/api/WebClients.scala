package ocs.api

import ocs.api.client.{AssemblyFeederJsClient, ListComponentsJsClient, SequenceEditorJsClient, SequencerCommandServiceJsClient}
import ocs.api.models.SequencerInfo
import org.scalajs.dom.raw.EventSource

object WebClients {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val gatewayHost = "http://localhost:9090"

  lazy val listSequencers: ListComponentsJsClient = new ListComponentsJsClient(new WebGateway(gatewayHost))

  def assemblyCommandClient(assemblyName: String): AssemblyFeederJsClient =
    new AssemblyFeederJsClient(new WebGateway(s"$gatewayHost/assembly/$assemblyName/"))

  def feeder(sequencerInfo: SequencerInfo): SequencerCommandServiceJsClient =
    new SequencerCommandServiceJsClient(makeGateway(sequencerInfo))
  def editor(sequencerInfo: SequencerInfo): SequenceEditorJsClient = new SequenceEditorJsClient(makeGateway(sequencerInfo))
  def results(sequencerInfo: SequencerInfo): EventSource           = makeGateway(sequencerInfo).source(SequenceResultsWeb.results)

  private def makeGateway(sequencerInfo: SequencerInfo) =
    new WebGateway(s"$gatewayHost/sequencer/${sequencerInfo.id}/${sequencerInfo.mode}/")

}
