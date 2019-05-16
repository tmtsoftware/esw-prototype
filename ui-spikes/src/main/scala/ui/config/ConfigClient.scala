package ui.config

import org.scalajs.dom.XMLHttpRequest
import org.scalajs.dom.ext.Ajax

import scala.concurrent.Future

object ConfigClient {

  private val baseUri = "http://localhost:5000/config/"

  def addConfig(path: String, message: String, token: String, configFileContent: String): Future[XMLHttpRequest] = {
    val url = s"$path?comment=$message"
    Ajax
      .post(
        url = s"$baseUri$url",
        headers = Map(
          "Content-Type"  → "text/plain",
          "Authorization" → s"Bearer $token"
        ),
        data = configFileContent
      )
  }

  def delete(path: String, token: String): Future[XMLHttpRequest] = {
    val url = s"$path?comment=delete file $path"
    Ajax
      .delete(
        url = s"$baseUri$url",
        headers = Map(
          "content-type"                   -> "application/json",
          "Authorization"                  → s"Bearer $token"
        )
      )
  }

}
