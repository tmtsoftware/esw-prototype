package tmt.sequencer

import org.scalajs.dom.ext.{Ajax, AjaxException}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class WebGateway(implicit ec: ExecutionContext) {
  def post(url: String): Future[Unit]               = post(url, "", println)
  def post(url: String, data: String): Future[Unit] = post(url, data, println)

  def post[T](url: String, data: String, transform: String => T): Future[T] =
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

  def get[T](url: String = "", transform: String => T): Future[T] =
    Ajax
      .get(
        url = url
      )
      .map { xhr =>
        println(xhr.responseText)
        transform(xhr.responseText)
      }
      .recover {
        case NonFatal(AjaxException(req)) => throw new RuntimeException(req.responseText)
      }
}
