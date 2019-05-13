package ui.config.components

import ocs.api.WebGateway
import typings.reactLib.reactMod.{FC, ^ ⇒ React}
import typings.stdLib.^.console
import ui.config.context.contexts.ConfigsContext
import ui.config.models.Item
import ui.todo.lib.JsUnit

import scala.concurrent.ExecutionContext.Implicits
import scala.util.control.NonFatal

object ConfigApp {
  import typings.reactLib.dsl._

  import Implicits.global

  private val gateway     = new WebGateway("http://localhost:5000/")
  private val configState = React.useContext(ConfigsContext)

  def fetchConfigs(): Unit = {
    println("=== UseEffect ===")
    gateway
      .get[Seq[Item]]("list")
      .map(configState.setItems)
      .recover {
        case NonFatal(e) ⇒ console.warn(e.getMessage)
      }
  }

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering Config App")

    React.useEffect(() ⇒ fetchConfigs())

    div.noprops(
      TMTTitleBar.Component.noprops()
    )

  }
}
