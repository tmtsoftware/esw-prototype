package tmt.ocs

import org.scalajs.dom.ext.{Ajax, AjaxException}
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class WebGateway(baseUri: String)(implicit ec: ExecutionContext) {

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

  def get[Response: Reads](url: String = ""): Future[Response] = {
    Ajax
      .get(
        url = s"$baseUri$url"
      )
      .map { xhr =>
        println(xhr.responseText)
        Json.parse(xhr.responseText).as[Response]
      }
      .recover {
        case NonFatal(AjaxException(req)) => throw new RuntimeException(req.responseText)
      }
  }
}
