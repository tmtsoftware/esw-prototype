package tmt.sequencer.models

import csw.messages.commands.CommandIssue._
import csw.messages.commands.CommandResponse._
import csw.messages.commands.CommandResultType.{Intermediate, Negative, Positive}
import csw.messages.commands.{CommandIssue, CommandResponse, CommandResultType, Result}
import csw.messages.params.models.Id
import play.api.libs.json._
import upickle.default.{macroRW, ReadWriter => RW, _}

trait UpickleRWSupport {

  import MicroPickleFormatAdapter._

  implicit val idRW: RW[Id]         = readWriterFromFormat
  implicit val resultRW: RW[Result] = readWriterFromFormat

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

  implicit lazy val commandResponseRW: RW[CommandResponse] = RW.merge(
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

  implicit lazy val commandResultTypeRW: RW[CommandResultType] = RW.merge(
    macroRW[Intermediate.type],
    RW.merge(macroRW[Positive.type], macroRW[Negative.type])
  )

}

object MicroPickleFormatAdapter {
  def readWriterFromFormat[T](implicit format: Format[T]): RW[T] = {
    upickle.default
      .readwriter[String]
      .bimap[T](
        result => format.writes(result).toString(),
        str => format.reads(Json.parse(str)).get
      )
  }

  def readWriterToFormat[T](implicit rw: RW[T]): Format[T] = {
    new Format[T] {
      override def reads(json: JsValue): JsResult[T] = JsSuccess(read[T](json.toString()))
      override def writes(o: T): JsValue             = Json.parse(write(o))
    }
  }
}
