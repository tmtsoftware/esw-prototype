package ocs.api

import akka.Done
import csw.location.api.models.AkkaLocation

import scala.concurrent.Future

trait ScriptRunnerApi {
  def loadScript(sequencerId: String, observingMode: String): Future[Either[AkkaLocation, AkkaLocation]]
  def stopScript: Future[Done]
  def getStatus: Future[Option[AkkaLocation]]
}
