package tmt.sequencer.client

import tmt.WebGateway
import tmt.sequencer.models.WebRWSupport

import scala.concurrent.Future

class ListComponentsClient(gateway: WebGateway) extends WebRWSupport {
  def listSequencers: Future[List[String]] =
    gateway.get("/locations/sequencers", transform = x => upickle.default.read[List[String]](x))

  def listAssemblies: Future[List[String]] =
    gateway.get("/locations/assemblies", transform = x => upickle.default.read[List[String]](x))
}
