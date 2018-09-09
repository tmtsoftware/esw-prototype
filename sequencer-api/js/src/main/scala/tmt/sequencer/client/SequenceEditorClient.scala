package tmt.sequencer.client

import csw.messages.commands.SequenceCommand
import csw.messages.params.models.Id
import play.api.libs.json.Json
import tmt.WebGateway
import tmt.sequencer.api.SequenceEditor
import tmt.sequencer.codecs.SequencerJsonSupport
import tmt.sequencer.models._

import scala.concurrent.Future

class SequenceEditorClient(gateway: WebGateway) extends SequenceEditor with SequencerJsonSupport {

  import csw.messages.params.formats.JsonSupport._

  override def addAll(commands: List[SequenceCommand]): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.AddAll}",
    data = Json.toJson(commands).toString()
  )

  override def pause(): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.Pause}"
  )

  override def resume(): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.Resume}"
  )

  override def reset(): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.Reset}"
  )

  override def sequence: Future[Sequence] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.Sequence}",
    data = "",
    transform = x => Json.parse(x).as[Sequence]
  )

  override def isAvailable: Future[Boolean] = ???

  override def delete(ids: List[Id]): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.Delete}",
    data = Json.toJson(ids).toString()
  )

  override def addBreakpoints(ids: List[Id]): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.AddBreakpoints}",
    data = Json.toJson(ids).toString()
  )

  override def removeBreakpoints(ids: List[Id]): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.RemoveBreakpoints}",
    data = Json.toJson(ids).toString()
  )

  override def insertAfter(id: Id, commands: List[SequenceCommand]): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.InsertAfter}",
    data = Json.toJson((id, commands)).toString()
  )

  override def prepend(commands: List[SequenceCommand]): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.Prepend}",
    data = Json.toJson(commands).toString()
  )

  override def replace(id: Id, commands: List[SequenceCommand]): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.Replace}",
    data = Json.toJson((id, commands)).toString()
  )

  override def shutdown(): Future[Unit] = gateway.post(
    url = s"${SequenceEditor.ApiName}/${SequenceEditor.Shutdown}"
  )
}
