package tmt.sequencer.client

import tmt.WebGateway
import tmt.sequencer.codecs.SequencerWebRWSupport

import scala.concurrent.Future

class ListComponentsClient(gateway: WebGateway) extends SequencerWebRWSupport {
  def listSequencers: Future[List[String]] =
    gateway.get(
      "/locations/sequencers",
      transform = x => upickle.default.read[List[String]](x)
    )

  def listAssemblies: Future[List[String]] =
    gateway.get(
      "/locations/assemblies",
      transform = x => upickle.default.read[List[String]](x)
    )

}
