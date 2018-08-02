package tmt.sequencer.client

import tmt.sequencer.WebGateway
import tmt.sequencer.models.WebRWSupport

import scala.concurrent.Future

class ListSequencersClient(gateway: WebGateway) extends WebRWSupport {
  def listSequencers: Future[List[String]] =
    gateway.get("/locations", transform = x => upickle.default.read[List[String]](x))
}
