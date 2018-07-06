package tmt.sequencer.models

import csw.messages.commands.CommandResponse

object CommandResponseConversions extends UpickleRWSupport {
  def asCommandResponse(commandResponseWeb: CommandResponseWeb): CommandResponse = {
    upickle.default.read[CommandResponse](commandResponseWeb.payload)
  }
  def fromCommandResponse(commandResponse: CommandResponse): CommandResponseWeb = CommandResponseWeb(
    commandResponse.runId.id,
    commandResponse.resultType.entryName,
    upickle.default.write(commandResponse)
  )
}
