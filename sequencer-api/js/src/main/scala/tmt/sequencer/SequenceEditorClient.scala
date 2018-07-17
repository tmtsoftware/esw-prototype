package tmt.sequencer

import org.scalajs.dom.ext.{Ajax, AjaxException}
import tmt.sequencer.api.SequenceEditorWeb
import tmt.sequencer.models._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class SequenceEditorClient(baseUri: String)(implicit ec: ExecutionContext) extends SequenceEditorWeb with WebRWSupport {

  private val EMPTY_DATA = ""

  override def addAll(commands: List[SequenceCommandWeb]): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.AddAll}",
    data = upickle.default.write(commands)
  )

  override def pause(): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Pause}",
    EMPTY_DATA
  )

  override def resume(): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Resume}",
    data = EMPTY_DATA
  )

  override def reset(): Future[Unit] = post(url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Reset}", EMPTY_DATA)

  override def sequenceWeb: Future[SequenceWeb] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Sequence}",
    transform = x => upickle.default.read[SequenceWeb](x)
  )

  override def delete(ids: List[String]): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Delete}",
    data = upickle.default.write(ids)
  )

  override def addBreakpoints(ids: List[String]): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.AddBreakpoints}",
    data = upickle.default.write(ids)
  )

  override def removeBreakpoints(ids: List[String]): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.RemoveBreakpoints}",
    data = upickle.default.write(ids)
  )

  override def insertAfter(id: String, commands: List[SequenceCommandWeb]): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.InsertAfter}",
    data = upickle.default.write((id, commands))
  )

  override def prepend(commands: List[SequenceCommandWeb]): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Prepend}",
    data = upickle.default.write(commands)
  )

  override def replace(id: String, commands: List[SequenceCommandWeb]): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Replace}",
    data = upickle.default.write((id, commands))
  )

  override def shutdown(): Future[Unit] = post(
    url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Shutdown}",
    data = EMPTY_DATA
  )

  private def post(url: String, data: String): Future[Unit] = post(url, data, println)

  private def post[T](url: String, data: String = "", transform: String => T): Future[T] =
    Ajax
      .post(
        url = url,
        data = data,
        headers = Map("Content-Type" -> "application/json")
      )
      .map { xhr =>
        println(xhr.responseText)
        transform(xhr.responseText)
      }
      .recover {
        case NonFatal(AjaxException(req)) => throw new RuntimeException(req.responseText)
      }
}
