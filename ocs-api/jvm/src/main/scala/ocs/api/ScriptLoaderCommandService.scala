package ocs.api
import akka.Done
import csw.location.api.models.ComponentId
import ocs.api.messages.ScriptCommand.Response

import scala.concurrent.Future

trait ScriptLoaderCommandService {
  def loadScript(sequencerId: String, observingMode: String): Future[Response[Done]]
  def stopScript: Future[Response[Done]]
  def getStatus: Future[Response[ComponentId]]
}
