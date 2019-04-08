package ocs.api

import csw.location.api.models.ComponentId
import ocs.api.messages.ScriptCommand.Response

import scala.concurrent.Future

trait ScriptLoaderCommandService {
  def loadScript(sequencerId: String, observingMode: String): Future[Response]
  def stopScript: Future[Response]
  def getStatus: Future[Option[ComponentId]]
}
