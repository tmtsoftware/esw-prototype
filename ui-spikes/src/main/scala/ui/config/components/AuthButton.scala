package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings.{contained, default}
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.{^ ⇒ Mui}
import typings.atMaterialDashUiCoreLib.buttonButtonMod.ButtonProps
import typings.atMaterialDashUiCoreLib.circularProgressCircularProgressMod.CircularProgressProps
import typings.cswDashAasDashJsLib.cswDashAasDashJsLibComponents.Consumer
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.AuthContext
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

  private def authButton(_onClick: AuthContext ⇒ Unit, text: String) = define.fc[JsUnit] { _ =>
    val props = ConsumerProps[AuthContext] { ctx ⇒
      div.props(
        HTMLAttributes(onClick = _ => _onClick(ctx)),
        Mui.Button.props(
          ButtonProps(
            style = buttonCss,
            variant = contained,
            color = default
          ),
          text
        )
      )
    }

    Consumer.props(props)
  }

  private val LoginButton  = authButton(_.login(), "Login")
  private val LogoutButton = authButton(_.logout(), "Logout")

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println("**** Rendering AuthButton")

    val props = ConsumerProps[AuthContext] { ctx ⇒
      val auth = ctx.auth
      if (auth == null || js.isUndefined(auth))
        Mui.CircularProgress.props(
          CircularProgressProps(color = atMaterialDashUiCoreLibStrings.inherit)
        )
      else {
        val isAuthenticated = auth.isAuthenticated.isDefined && auth.isAuthenticated.get()
        if (isAuthenticated) LogoutButton.noprops()
        else LoginButton.noprops()
      }
    }

    Consumer.props(props)
  }

}
