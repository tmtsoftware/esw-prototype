package tmt.sequencer.rpc.server

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import tmt.sequencer.models.SequencerOperationFailed

object SequencerExceptionHandler extends Directives with JsonRejectionHandler {

  def route: Directive[Unit] =
    handleExceptions(jsonExceptionHandler) & handleRejections(jsonRejectionHandler) & rejectEmptyResponse

  private val jsonExceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: SequencerOperationFailed =>
      complete(JsonSupport.asJsonResponse(StatusCodes.InternalServerError, ex.getMessage))
  }
}
