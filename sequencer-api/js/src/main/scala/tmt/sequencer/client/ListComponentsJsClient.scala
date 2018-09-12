package tmt.sequencer.client

import tmt.WebGateway
import tmt.ocs.codecs.SequencerJsonSupport

import scala.concurrent.Future

class ListComponentsJsClient(gateway: WebGateway) extends SequencerJsonSupport {
  def listSequencers: Future[List[String]] = gateway.get[List[String]]("/locations/sequencers")
  def listAssemblies: Future[List[String]] = gateway.get[List[String]]("/locations/assemblies")
}
