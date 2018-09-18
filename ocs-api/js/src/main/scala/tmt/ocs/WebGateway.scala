package tmt.ocs

import org.scalajs.dom.EventSource
import org.scalajs.dom.ext.{Ajax, AjaxException}
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class WebGateway(baseUri: String = "http://localhost:9090")(implicit ec: ExecutionContext) {

  implicit val unitReads: Reads[Unit] = Reads(_ => JsSuccess(()))

  def postOneway[Request: Writes](url: String, entity: Request = ""): Future[Unit] = {
    post[Request, Unit](url, entity)
  }

  def post[Request: Writes, Response: Reads](url: String, entity: Request = ""): Future[Response] = {
    Ajax
      .post(
        url = s"$baseUri$url",
        data = Json.toJson(entity).toString(),
        headers = Map("content-type" -> "application/json")
      )
      .map { xhr =>
        xhr.getResponseHeader("content-type") match {
          case "application/json" => Json.parse(xhr.responseText).as[Response]
          case _                  => JsString(xhr.responseText).as[Response]
        }
      }
      .recover {
        case NonFatal(AjaxException(req)) => throw new RuntimeException(req.responseText)
      }
  }

  def get[Response: Reads](url: String): Future[Response] = {
    Ajax
      .get(
        url = s"$baseUri$url"
      )
      .map { xhr =>
        Json.parse(xhr.responseText).as[Response]
      }
      .recover {
        case NonFatal(AjaxException(req)) => throw new RuntimeException(req.responseText)
      }
  }

  def source(url: String): EventSource                            = new EventSource(s"$baseUri$url")
  def stream[Response: Reads](url: String): EventStream[Response] = new EventStream(source(url))
}
