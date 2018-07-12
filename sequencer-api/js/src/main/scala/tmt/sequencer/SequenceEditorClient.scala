package tmt.sequencer

import org.scalajs.dom.ext.Ajax
import tmt.sequencer.api.SequenceEditorWeb
import tmt.sequencer.models._

import scala.concurrent.{ExecutionContext, Future}

class SequenceEditorClient(baseUri: String)(implicit ec: ExecutionContext) extends SequenceEditorWeb with WebRWSupport {

  override def addAll(commands: List[SequenceCommandWeb]): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.AddAll}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write(commands),
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def pause(): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Pause}"
    Ajax
      .post(
        url = url,
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def resume(): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Resume}"
    Ajax
      .post(
        url = url,
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def reset(): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Reset}"
    Ajax
      .post(
        url = url,
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def sequenceWeb: Future[SequenceWeb] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Sequence}"
    Ajax
      .post(
        url = url,
        headers = Map("Content-Type" -> "application/json")
      )
      .map { xhr =>
        println(xhr.responseText)
        upickle.default.read[SequenceWeb](xhr.responseText)
      }
  }

  override def delete(ids: List[String]): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Delete}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write(ids),
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def addBreakpoints(ids: List[String]): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.AddBreakpoints}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write(ids),
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def removeBreakpoints(ids: List[String]): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.RemoveBreakpoints}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write(ids),
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def insertAfter(id: String, commands: List[SequenceCommandWeb]): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.InsertAfter}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write((id, commands)),
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def prepend(commands: List[SequenceCommandWeb]): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Prepend}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write(commands),
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def replace(id: String, commands: List[SequenceCommandWeb]): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Replace}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write((id, commands)),
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }

  override def shutdown(): Future[Unit] = {
    val url = s"$baseUri/${SequenceEditorWeb.ApiName}/${SequenceEditorWeb.Shutdown}"
    Ajax
      .post(
        url = url,
        headers = Map("Content-Type" -> "application/json")
      )
      .map(xhr => println(xhr.responseText))
  }
}
