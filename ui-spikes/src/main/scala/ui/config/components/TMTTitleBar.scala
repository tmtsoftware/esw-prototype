package ui.config.components

import typings.atMaterialDashUiCoreLib.appBarAppBarMod.AppBarProps
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.{^ ⇒ Mui}
import typings.atMaterialDashUiCoreLib.typographyTypographyMod.TypographyProps
import typings.reactLib.dsl.define
import typings.reactLib.reactMod
import typings.reactLib.reactMod.{FC, HTMLAttributes}
import ui.todo.lib.JsUnit

object TMTTitleBar {

  private val flex = new reactMod.CSSProperties {
    flexGrow = 1
  }

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering TMTTitleBar")

    import typings.reactLib.dsl._
    div.props(
      HTMLAttributes(style = flex),
      Mui.AppBar.props(
        AppBarProps(position = static),
        Mui.Toolbar.noprops(
          Mui.Typography.props(
            TypographyProps(
              style = flex,
              variant = atMaterialDashUiCoreLibStrings.h6,
              color = inherit
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
