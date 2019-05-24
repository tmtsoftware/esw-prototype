package ui.config.components

import typings.atMaterialDashUiCoreLib.appBarAppBarMod.AppBarProps
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.{^ ⇒ Mui}
import typings.atMaterialDashUiCoreLib.stylesCreateMuiThemeMod.Theme
import typings.atMaterialDashUiCoreLib.stylesWithStylesMod.CSSProperties
import typings.reactLib.reactMod.{FC, HTMLAttributes}
import ui.config.components.PropsFactory.typographyProps
import ui.config.components.utils.StyledFC
import ui.todo.lib.JsUnit

import scala.scalajs.js

object TMTTitleBar {

  trait StyleOverrides[T] extends js.Object {
    val root: T
    val grow: T
    val menuButton: T
  }

  val styles: js.Function1[Theme, StyleOverrides[CSSProperties]] = _ =>
    new StyleOverrides[CSSProperties] {
      override val root: CSSProperties = new CSSProperties {
        flexGrow = 1
      }

      override val grow: CSSProperties = new CSSProperties {
        flexGrow = 1
      }

      override val menuButton: CSSProperties = new CSSProperties {
        marginLeft = -12
        marginRight = 20
      }
  }

  val Component: FC[JsUnit] = StyledFC[StyleOverrides, JsUnit](styles) { props =>
    println(s"**** rendering TMTTitleBar")

    import typings.reactLib.dsl._

    div.props(
      HTMLAttributes(className = props.classes.root),
      Mui.AppBar.props(
        AppBarProps(position = static),
        Mui.Toolbar.noprops(
          Mui.Typography.props(
            typographyProps(
              _className = props.classes.grow,
              _color = inherit,
              _variant = atMaterialDashUiCoreLibStrings.h6
            ),
            "TMT CSW Configurations"
          ),
          UserInfo.Component.noprops(),
          AuthButton.Component.noprops()
        )
      )
    )

  }

}
