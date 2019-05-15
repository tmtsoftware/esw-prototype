package ui.config.components

import ocs.api.WebGateway
import typings.reactLib.reactMod.{FC, ^ ⇒ React}
import ui.config.context.contexts.Context.ConfigStore
import ui.config.models.Item
import ui.todo.lib.JsUnit

import scala.concurrent.ExecutionContext.Implicits
import scala.scalajs.js

object ConfigApp {
  import typings.reactLib.dsl._

  import Implicits.global
  val gateway = new WebGateway("http://localhost:5000/")

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering Config App")
    val (_, setItems) = ConfigStore.use()

    val fetchConfigs: js.Function0[Unit] =
      () ⇒
        gateway
          .get[Seq[Item]]("list")
          .map(setItems)

    React.useEffect(fetchConfigs, js.Array())

    div.noprops(
      TMTTitleBar.Component.noprops(),
      ConfigCollection.Component.noprops(),
      AddButton.Component.noprops()
    )

  }
}
