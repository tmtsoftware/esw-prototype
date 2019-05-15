package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents.Grid
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibNumbers._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.gridGridMod.GridProps
import typings.reactLib.reactMod
import typings.reactLib.reactMod.FC
import ui.config.components.ConfigPreview.ConfigPreviewProps
import ui.config.context.contexts.Context.ConfigStore
import ui.todo.lib.JsUnit

object ConfigCollection {
  import typings.reactLib.dsl._
  private val style0 = new reactMod.CSSProperties {
    marginRight = "10px"
  }

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering ConfigCollection")
    val items = ConfigStore.useGetter()

    println(s"Items : ${items.size}")

    val s = items.map { i ⇒
      Grid.props(
        GridProps(style = style0, item = true),
        ConfigPreview.Component.props(ConfigPreviewProps(i))
      )
    }

    Grid.props(
      GridProps(
        style = style0,
        container = true,
        justify = `flex-start`,
        spacing = `8`
      ),
      s: _*
    )
  }

}
