package ui.config.components

import typings.atMaterialDashUiCoreLib.stylesCreateMuiThemeMod.Theme
import typings.atMaterialDashUiCoreLib.stylesWithStylesMod.CSSProperties
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.CheckLogin
import typings.cswDashAasDashJsLib.distComponentsAuthMod.Auth
import typings.cswDashAasDashJsLib.distComponentsAuthenticationCheckLoginMod.CheckLoginProps
import typings.cswDashAasDashJsLib.distComponentsContextAuthContextMod.AuthContextType
import typings.cswDashAasDashJsLib.{cswDashAasDashJsMod ⇒ AAS}
import typings.reactLib.reactMod.{FC, HTMLAttributes, ^ ⇒ React}
import ui.config.components.utils.StyledFC
import ui.todo.lib.JsUnit

import scala.scalajs.js

object UserInfo {
  import typings.reactLib.dsl._

  trait StyleOverrides[T] extends js.Object {
    val userInfo: T
  }

  val styles: js.Function1[Theme, StyleOverrides[CSSProperties]] = _ =>
    new StyleOverrides[CSSProperties] {
      override val userInfo: CSSProperties = new CSSProperties {
        marginRight = "20px"
      }
    }

  val Component: FC[JsUnit] = StyledFC[StyleOverrides, JsUnit](styles) { props =>
    println(s"**** rendering UserInfo")

    val ctx = React.useContext(AAS.AuthContext)

    CheckLogin(
      CheckLoginProps(
        children = span.props(
          HTMLAttributes(className = props.classes.userInfo),
          greetings(ctx)
        ),
        error = null
      )
    )
  }

  private def greetings(ctx: AuthContextType) =
    if (ctx == null ||
        ctx.auth == null ||
        js.isUndefined(ctx.auth.merge[Auth].tokenParsed))
      ""
    else "Hello " + ctx.auth.merge[Auth].tokenParsed.get.asInstanceOf[js.Dynamic].preferred_username.asInstanceOf[String]
}
