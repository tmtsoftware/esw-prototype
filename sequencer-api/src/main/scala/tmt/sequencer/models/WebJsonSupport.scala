package tmt.sequencer.models

import csw.messages.commands._
import csw.messages.extensions.Formats
import csw.messages.extensions.Formats.MappableFormat
import csw.messages.params.generics.Parameter
import csw.messages.params.models.{ObsId, Prefix}
import julienrf.json.derived
import play.api.libs.json._
import tmt.assembly.codecs.AssemblyWebJsonSupport
import tmt.sequencer.codecs.SequencerWebJsonSupport

trait WebJsonSupport extends SequencerWebJsonSupport with AssemblyWebJsonSupport {

  implicit lazy val aggregateResponseFormat: OFormat[AggregateResponse] = Json.format
  implicit lazy val commandListFormat: OFormat[CommandList]             = Json.format
  implicit lazy val stepFormat: OFormat[Step]                           = Json.format
  implicit lazy val sequenceFormat: OFormat[Sequence]                   = Json.format

  implicit lazy val commandResponseFormat: Format[CommandResponse] = Formats
    .of[CommandResponseWeb]
    .bimap[CommandResponse](
      x => CommandResponseWeb(Json.toJson(x)(commandResponseFormatHelper).as[JsObject]),
      x => x.response.as[CommandResponse](commandResponseFormatHelper)
    )

  implicit lazy val sequenceCommandFormat: Format[SequenceCommand] = Formats
    .of[SequenceCommandWeb]
    .bimap[SequenceCommand](
      command =>
        SequenceCommandWeb(
          command.getClass.getSimpleName,
          command.source.prefix,
          command.commandName.name,
          command.maybeObsId.map(_.obsId),
          Json.toJson(command.paramSet).as[JsArray],
          Option(command.runId.id)
      ),
      command =>
        command.kind match {
          case "Setup" =>
            Setup(
              Prefix(command.source),
              CommandName(command.commandName),
              command.maybeObsId.map(v => ObsId(v)),
              command.paramSet.as[Set[Parameter[_]]]
            )
          case "Observe" =>
            Observe(
              Prefix(command.source),
              CommandName(command.commandName),
              command.maybeObsId.map(v => ObsId(v)),
              command.paramSet.as[Set[Parameter[_]]]
            )
          case _ => ???
      }
    )

  implicit lazy val commandIssueFormat: OFormat[CommandIssue]    = derived.flat.oformat((__ \ "type").format[String])
  lazy val commandResponseFormatHelper: OFormat[CommandResponse] = derived.flat.oformat((__ \ "type").format[String])
}
