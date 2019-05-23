package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings.{contained, default}
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.{^ ⇒ Mui}
import typings.atMaterialDashUiCoreLib.circularProgressCircularProgressMod.CircularProgressProps
import typings.cswDashAasDashJsLib.{ cswDashAasDashJsMod ⇒ AAS }
import typings.cswDashAasDashJsLib.distComponentsAuthMod.Auth
import typings.cswDashAasDashJsLib.distComponentsContextAuthContextMod.AuthContextType
import typings.reactLib.reactMod
import typings.reactLib.reactMod._
import ui.config.components.PropsFactory.buttonProps
import ui.todo.lib.JsUnit

import scala.scalajs.js

object AuthButton {
  import typings.reactLib.dsl._

  private val buttonCss = new reactMod.CSSProperties {
    marginLeft = -12
    marginRight = 20
  }

  private def authButton(ctx: AuthContextType, _onClick: AuthContextType ⇒ Unit, text: String) = define.fc[JsUnit] { _ =>
    Mui.Button.props(
      buttonProps(_color = default, _onClick = _ ⇒ _onClick(ctx), _variant = contained, _style = buttonCss),
      text
    )
  }

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println("**** Rendering AuthButton")
    val ctx = ^.useContext(AAS.AuthContext)

    val auth = ctx.auth
    if (auth == null || js.isUndefined(auth))
      Mui.CircularProgress.props(
        CircularProgressProps(color = atMaterialDashUiCoreLibStrings.inherit)
      )
    else {
      val isAuthenticated = {
        val authenticated = auth.merge[Auth].isAuthenticated
        authenticated.isDefined && authenticated.get
      }
      if (isAuthenticated) authButton(ctx, _.logout(), "Logout").noprops()
      else authButton(ctx, _.login(), "Login").noprops()
    }

  }

}
