package ocs.api.client

import ocs.api.WebGateway
import ocs.api.codecs.SequencerJsonSupport

import scala.concurrent.Future

class ListComponentsJsClient(gateway: WebGateway) extends SequencerJsonSupport {
  def listSequencers: Future[List[String]] = gateway.get[List[String]]("/locations/sequencers")
  def listAssemblies: Future[List[String]] = gateway.get[List[String]]("/locations/assemblies")
}
