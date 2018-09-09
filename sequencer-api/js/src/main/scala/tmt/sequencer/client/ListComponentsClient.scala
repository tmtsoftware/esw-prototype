package tmt.sequencer.client

import play.api.libs.json.Json
import tmt.WebGateway
import tmt.sequencer.codecs.SequencerJsonSupport

import scala.concurrent.Future

class ListComponentsClient(gateway: WebGateway) extends SequencerJsonSupport {
  def listSequencers: Future[List[String]] =
    gateway.get(
      "/locations/sequencers",
      transform = x => Json.parse(x).as[List[String]]
    )

  def listAssemblies: Future[List[String]] =
    gateway.get(
      "/locations/assemblies",
      transform = x => Json.parse(x).as[List[String]]
    )

}
