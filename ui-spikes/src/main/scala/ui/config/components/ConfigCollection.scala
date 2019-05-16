package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents.Grid
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibNumbers._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.reactLib.reactMod
import typings.reactLib.reactMod.FC
import ui.config.components.ConfigPreview.ConfigPreviewProps
import ui.config.components.PropsFactory.gridProps
import ui.config.context.contexts.Context.ConfigStore
import ui.todo.lib.JsUnit

object ConfigCollection {
  import typings.reactLib.dsl._
  private val style0 = new reactMod.CSSProperties {
    marginRight = "10px"
  }

  private val style1 = new reactMod.CSSProperties {
    padding = "10px"
  }

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering ConfigCollection")
    val items = ConfigStore.useGetter()

    lazy val configs = items.map { i ⇒
      Grid.props(
        gridProps(_item = true, _style = style0),
        ConfigPreview.Component.props(ConfigPreviewProps(i))
      )
    }

    Grid.props(
      gridProps(_container = true, _justify = `flex-start`, _spacing = `8`, _style = style1),
      configs: _*
    )
  }

}
