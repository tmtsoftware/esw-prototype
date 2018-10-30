package ocs.api

import csw.params.commands.CommandResponse.SubmitResponse
import ocs.api.models.Sequence

import scala.concurrent.Future

trait SequencerCommandService {
  def submit(commandList: Sequence): Future[SubmitResponse]
}

object SequencerCommandService {
  val ApiName = "feeder"
  val Feed    = "feed"
}
