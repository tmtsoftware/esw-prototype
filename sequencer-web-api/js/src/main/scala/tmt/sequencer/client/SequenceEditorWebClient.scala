package tmt.sequencer.client

import play.api.libs.json.Json
import tmt.WebGateway
import tmt.sequencer.api.SequenceEditorWeb
import tmt.sequencer.codecs.SequencerWebJsonSupport
import tmt.sequencer.models._

import scala.concurrent.Future

class SequenceEditorWebClient(gateway: WebGateway) extends SequenceEditorWeb with SequencerWebJsonSupport {

  override def addAll(commands: List[SequenceCommandWeb]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.AddAll}",
    data = Json.toJson(commands).toString()
  )

  override def pause(): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Pause}"
  )

  override def resume(): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Resume}"
  )

  override def reset(): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Reset}"
  )

  override def sequenceWeb: Future[SequenceWeb] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Sequence}",
    data = "",
    transform = x => Json.parse(x).as[SequenceWeb]
  )

  override def delete(ids: List[String]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Delete}",
    data = Json.toJson(ids).toString()
  )

  override def addBreakpoints(ids: List[String]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.AddBreakpoints}",
    data = Json.toJson(ids).toString()
  )

  override def removeBreakpoints(ids: List[String]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.RemoveBreakpoints}",
    data = Json.toJson(ids).toString()
  )

  override def insertAfter(id: String, commands: List[SequenceCommandWeb]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.InsertAfter}",
    data = Json.toJson((id, commands)).toString()
  )

  override def prepend(commands: List[SequenceCommandWeb]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Prepend}",
    data = Json.toJson(commands).toString()
  )

  override def replace(id: String, commands: List[SequenceCommandWeb]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Replace}",
    data = Json.toJson((id, commands)).toString()
  )

  override def shutdown(): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Shutdown}"
  )

}
