package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents.Grid
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibNumbers._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.stylesCreateMuiThemeMod.Theme
import typings.atMaterialDashUiCoreLib.stylesWithStylesMod.CSSProperties
import typings.reactLib.reactMod.FC
import ui.config.components.ConfigPreview.ConfigPreviewProps
import ui.config.components.PropsFactory.gridProps
import ui.config.components.utils.StyledFC
import ui.config.context.contexts.Context.ConfigStore
import ui.todo.lib.JsUnit

import scala.scalajs.js

object ConfigCollection {
  import typings.reactLib.dsl._

  trait StyleOverrides[T] extends js.Object {
    val list: T
  }

  val styles: js.Function1[Theme, StyleOverrides[CSSProperties]] = _ =>
    new StyleOverrides[CSSProperties] {
      override val list: CSSProperties = new CSSProperties {
        padding = "10px"
      }
  }

  val Component: FC[JsUnit] = StyledFC[StyleOverrides, JsUnit](styles) { props =>
    println(s"**** rendering ConfigCollection")
    val items = ConfigStore.useGetter()

    lazy val configs = items.map { i ⇒
      Grid.props(
        gridProps(_item = true),
        ConfigPreview.Component.props(ConfigPreviewProps(i))
      )
    }

    Grid.props(
      gridProps(_className = props.classes.list, _container = true, _justify = `flex-start`, _spacing = `8`),
      configs: _*
    )
  }

}
