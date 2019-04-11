package ocs.api.client

import csw.params.commands.SequenceCommand
import csw.params.core.models.Id
import ocs.api.codecs.SequencerJsonSupport
import ocs.api.models.StepList
import ocs.api.{SequenceEditor, WebGateway}

import scala.concurrent.Future

class SequenceEditorJsClient(gateway: WebGateway) extends SequenceEditor with SequencerJsonSupport {

  override def addAll(commands: List[SequenceCommand]): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.AddAll}",
    commands
  )

  override def pause(): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.Pause}"
  )

  override def resume(): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.Resume}"
  )

  override def reset(): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.Reset}"
  )

  override def status: Future[StepList] = gateway.get[StepList](
    s"${SequenceEditor.ApiName}/${SequenceEditor.Sequence}"
  )

  override def isAvailable: Future[Boolean] = ???

  override def delete(ids: List[Id]): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.Delete}",
    ids
  )

  override def addBreakpoints(ids: List[Id]): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.AddBreakpoints}",
    ids
  )

  override def removeBreakpoints(ids: List[Id]): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.RemoveBreakpoints}",
    ids
  )

  override def insertAfter(id: Id, commands: List[SequenceCommand]): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.InsertAfter}",
    (id, commands)
  )

  override def prepend(commands: List[SequenceCommand]): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.Prepend}",
    commands
  )

  override def replace(id: Id, commands: List[SequenceCommand]): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.Replace}",
    (id, commands)
  )

  override def shutdown(): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.Shutdown}"
  )

  override def abort(): Future[Unit] = gateway.postOneway(
    s"${SequenceEditor.ApiName}/${SequenceEditor.Abort}"
  )
}
