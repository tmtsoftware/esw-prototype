package tmt.sequencer.models

import csw.messages.commands.{CommandName, Observe, SequenceCommand, Setup}
import csw.messages.params.models.{ObsId, Prefix}
import play.api.libs.json.Json
import ujson.Js
import upickle.default.{macroRW, ReadWriter => RW}
import csw.messages.params.formats.JsonSupport._

case class InputCommand(
    kind: String,
    runId: String,
    source: String,
    commandName: String,
    maybeObsId: Option[String],
    paramSet: Js.Arr
)

object InputCommand {
  implicit val inputCommandRW: RW[InputCommand] = macroRW[InputCommand]

  def fromSequenceCommand(command: SequenceCommand): InputCommand = {
    InputCommand(
      command.getClass.getSimpleName,
      command.runId.toString,
      command.source.prefix,
      command.commandName.name,
      command.maybeObsId.map(_.obsId),
      ujson.read((Json.toJson(command) \ "paramSet").toString()).arr
    )
  }

  def asSequenceCommand(command: InputCommand): SequenceCommand = {
    command.kind match {
      case "Setup" =>
        Setup(
          Prefix(command.source),
          CommandName(command.commandName),
          command.maybeObsId.map(v => ObsId(v)),
          paramSetFormat.reads(Json.parse(command.paramSet.toString())).getOrElse(Set.empty)
        )
      case "Observe" =>
        Observe(
          Prefix(command.source),
          CommandName(command.commandName),
          command.maybeObsId.map(v => ObsId(v)),
          paramSetFormat.reads(Json.parse(command.paramSet.toString())).getOrElse(Set.empty)
        )
      case "Wait" => ???
      case _      => ???
    }
  }
}
