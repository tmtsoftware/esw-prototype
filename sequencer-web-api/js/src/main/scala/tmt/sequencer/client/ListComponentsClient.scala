package tmt.sequencer.client

import tmt.WebGateway
import tmt.sequencer.codecs.SequencerRWSupport

import scala.concurrent.Future

class ListComponentsClient(gateway: WebGateway) extends SequencerRWSupport {
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
