package tmt

import org.scalajs.dom.ext.{Ajax, AjaxException}
import org.scalajs.dom.window

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class WebGateway(implicit ec: ExecutionContext) {
  def post(url: String): Future[Unit]               = post(url, "", println)
  def post(url: String, data: String): Future[Unit] = post(url, data, println)

  def post[T](url: String, data: String, transform: String => T): Future[T] =
    Ajax
      .post(
        url = s"http://localhost:9090/${window.location.hash.drop(1)}$url",
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
        url = s"http://localhost:9090/${window.location.hash.drop(1)}$url"
      )
      .map { xhr =>
        println(xhr.responseText)
        transform(xhr.responseText)
      }
      .recover {
        case NonFatal(AjaxException(req)) => throw new RuntimeException(req.responseText)
      }
}
