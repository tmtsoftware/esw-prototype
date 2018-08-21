package tmt.sequencer.client

import tmt.WebGateway
import tmt.sequencer.api.SequenceEditorWeb
import tmt.sequencer.codecs.SequencerWebRWSupport
import tmt.sequencer.models._

import scala.concurrent.Future

class SequenceEditorWebClient(gateway: WebGateway) extends SequenceEditorWeb with SequencerWebRWSupport {

  override def addAll(commands: List[SequenceCommandWeb]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.AddAll}",
    data = upickle.default.write(commands)
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
    transform = x => upickle.default.read[SequenceWeb](x)
  )

  override def delete(ids: List[String]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Delete}",
    data = upickle.default.write(ids)
  )

  override def addBreakpoints(ids: List[String]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.AddBreakpoints}",
    data = upickle.default.write(ids)
  )

  override def removeBreakpoints(ids: List[String]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.RemoveBreakpoints}",
    data = upickle.default.write(ids)
  )

  override def insertAfter(id: String, commands: List[SequenceCommandWeb]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.InsertAfter}",
    data = upickle.default.write((id, commands))
  )

  override def prepend(commands: List[SequenceCommandWeb]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Prepend}",
    data = upickle.default.write(commands)
  )

  override def replace(id: String, commands: List[SequenceCommandWeb]): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Replace}",
    data = upickle.default.write((id, commands))
  )

  override def shutdown(): Future[Unit] = gateway.post(
    url = s"${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Shutdown}"
  )

}
