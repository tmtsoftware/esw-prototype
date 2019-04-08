package ocs.api

import akka.Done
import csw.location.api.models.ComponentId

import scala.concurrent.Future

trait ScriptLoaderCommandService {
  def loadScript(sequencerId: String, observingMode: String): Future[Either[String, Done]]
  def stopScript: Future[Done]
  def getStatus: Future[Option[ComponentId]]
}
