package ui.config

import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future

object ConfigClient {

  private val baseUri = "http://localhost:5000/"

  def delete(path: String, token: String): Future[XMLHttpRequest] = {
    val url = s"config/$path?comment=delete file $path"
    Ajax
      .delete(
        url = s"$baseUri$url",
        headers = Map(
          "content-type"                   -> "application/json",
          "Authorization"                  → s"Bearer $token",
          "Access-Control-Request-Headers" → "x-requested-with"
        )
      )
  }

}