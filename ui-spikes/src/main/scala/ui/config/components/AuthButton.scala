package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings.{contained, default}
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.{^ ⇒ Mui}
import typings.atMaterialDashUiCoreLib.buttonButtonMod.ButtonProps
import typings.atMaterialDashUiCoreLib.circularProgressCircularProgressMod.CircularProgressProps
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.{Auth, IAuthContext, ^ ⇒ AAS}
import typings.reactLib.reactMod
import typings.reactLib.reactMod._
import ui.todo.lib.JsUnit

import scala.scalajs.js

object AuthButton {
  import typings.reactLib.dsl._

  private val buttonCss = new reactMod.CSSProperties {
    marginLeft = -12
    marginRight = 20
  }

  private def authButton(ctx: IAuthContext, _onClick: IAuthContext ⇒ Unit, text: String) = define.fc[JsUnit] { _ =>
    val buttonProps = ButtonProps(action = null, color = default, onClick = _ ⇒ _onClick(ctx))
    buttonProps.variant = contained
    buttonProps.style = buttonCss
    Mui.Button.props(buttonProps, text)
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
        authenticated.isDefined && authenticated.get().isDefined && authenticated.get().get
      }
      if (isAuthenticated) authButton(ctx, _.logout(), "Logout").noprops()
      else authButton(ctx, _.login(), "Login").noprops()
    }

  }

}
