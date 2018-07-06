package tmt.sequencer.rpc.server

import csw.messages.params.models.Id
import tmt.sequencer.api.{SequenceEditor, SequenceEditorWeb}
import tmt.sequencer.models.{Sequence, SequenceCommandConversions, SequenceCommandWeb, SequenceWeb}

import scala.concurrent.{ExecutionContext, Future}

class SequenceEditorWebImpl(sequenceEditor: SequenceEditor)(implicit ec: ExecutionContext) extends SequenceEditorWeb {

  override def addAll(commands: List[SequenceCommandWeb]): Future[Unit] =
    sequenceEditor.addAll(commands.map(SequenceCommandConversions.asSequenceCommand))

  override def pause(): Future[Unit] = sequenceEditor.pause()

  override def resume(): Future[Unit] = sequenceEditor.resume()

  override def reset(): Future[Unit] = sequenceEditor.reset()

  override def sequenceWeb: Future[SequenceWeb] = sequenceEditor.sequence.map(Sequence.toSequenceWeb)

  override def delete(ids: List[String]): Future[Unit] = sequenceEditor.delete(ids.map(Id.apply))

  override def addBreakpoints(ids: List[String]): Future[Unit] = sequenceEditor.addBreakpoints(ids.map(Id.apply))

  override def removeBreakpoints(ids: List[String]): Future[Unit] = sequenceEditor.removeBreakpoints(ids.map(Id.apply))

  override def insertAfter(id: String, commands: List[SequenceCommandWeb]): Future[Unit] =
    sequenceEditor.insertAfter(Id(id), commands.map(SequenceCommandConversions.asSequenceCommand))

  override def prepend(commands: List[SequenceCommandWeb]): Future[Unit] =
    sequenceEditor.prepend(commands.map(SequenceCommandConversions.asSequenceCommand))

  override def replace(id: String, commands: List[SequenceCommandWeb]): Future[Unit] =
    sequenceEditor.replace(Id(id), commands.map(SequenceCommandConversions.asSequenceCommand))

  override def shutdown(): Future[Unit] = sequenceEditor.shutdown()
}
