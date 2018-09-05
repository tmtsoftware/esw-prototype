package tmt.sequencer.models

import csw.messages.commands.CommandIssue._
import csw.messages.commands.CommandResponse._
import csw.messages.commands._
import csw.messages.params.generics.Parameter
import csw.messages.params.models.{Id, ObsId, Prefix}
import play.api.libs.json.Format
import tmt.assembly.codecs.AssemblyWebRWSupport
import tmt.sequencer.codecs.SequencerRWSupport
import tmt.utils.UpickleFormatAdapter
import upickle.default.{macroRW, ReadWriter => RW, _}

trait UpickleRWSupport extends SequencerRWSupport with AssemblyWebRWSupport {

  implicit lazy val idRW: RW[Id]         = UpickleFormatAdapter.playJsonToUpickle
  implicit lazy val resultRW: RW[Result] = UpickleFormatAdapter.playJsonToUpickle
  implicit lazy val paramSetRW: RW[Set[Parameter[_]]] =
    UpickleFormatAdapter.playJsonToUpickle(implicitly[Format[Set[Parameter[_]]]])

  implicit lazy val aggregateResponseRW: RW[AggregateResponse] = macroRW
  implicit lazy val commandListRW: RW[CommandList]             = macroRW
  implicit lazy val stepRW: RW[Step]                           = macroRW
  implicit lazy val sequenceRW: RW[Sequence]                   = macroRW

  implicit lazy val commandResponseRW: RW[CommandResponse] = readwriter[CommandResponseWeb].bimap(
    x => CommandResponseWeb(writeJs(x)(commandResponseRWHelper).obj),
    x => readJs(x.response)(commandResponseRWHelper)
  )

  implicit lazy val sequenceCommandRW: RW[SequenceCommand] = readwriter[SequenceCommandWeb].bimap(
    command =>
      SequenceCommandWeb(
        command.getClass.getSimpleName,
        command.source.prefix,
        command.commandName.name,
        command.maybeObsId.map(_.obsId),
        writeJs(command.paramSet)(paramSetRW).arr,
        Option(command.runId.id)
    ),
    command =>
      command.kind match {
        case "Setup" =>
          Setup(
            Prefix(command.source),
            CommandName(command.commandName),
            command.maybeObsId.map(v => ObsId(v)),
            readJs(command.paramSet)(paramSetRW)
          )
        case "Observe" =>
          Observe(
            Prefix(command.source),
            CommandName(command.commandName),
            command.maybeObsId.map(v => ObsId(v)),
            readJs(command.paramSet)(paramSetRW)
          )
        case "Wait" => ???
        case _      => ???
    }
  )

  implicit lazy val commandIssueRW: RW[CommandIssue] = RW.merge(
    macroRW[MissingKeyIssue],
    macroRW[WrongPrefixIssue],
    macroRW[WrongUnitsIssue],
    macroRW[WrongNumberOfParametersIssue],
    macroRW[WrongParameterTypeIssue],
    macroRW[AssemblyBusyIssue],
    macroRW[UnresolvedLocationsIssue],
    macroRW[ParameterValueOutOfRangeIssue],
    macroRW[WrongInternalStateIssue],
    macroRW[UnsupportedCommandInStateIssue],
    macroRW[UnsupportedCommandIssue],
    macroRW[RequiredServiceUnavailableIssue],
    macroRW[RequiredHCDUnavailableIssue],
    macroRW[RequiredAssemblyUnavailableIssue],
    macroRW[RequiredSequencerUnavailableIssue],
    macroRW[ComponentLockedIssue],
    macroRW[OtherIssue],
  )

  lazy val commandResponseRWHelper: RW[CommandResponse] = RW.merge(
    macroRW[Accepted],
    macroRW[Invalid],
    macroRW[CompletedWithResult],
    macroRW[Completed],
    macroRW[NoLongerValid],
    macroRW[Error],
    macroRW[Cancelled],
    macroRW[CommandNotAvailable],
    macroRW[NotAllowed]
  )
}
