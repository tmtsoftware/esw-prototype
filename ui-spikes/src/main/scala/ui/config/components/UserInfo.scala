package ui.config.components

import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.{Auth, CheckLoginProps, IAuthContext, ^ ⇒ AAS}
import typings.reactLib.reactMod.{CSSProperties, FC, HTMLAttributes, ^ ⇒ React}
import ui.todo.lib.JsUnit

import scala.scalajs.js

object UserInfo {
  import typings.reactLib.dsl._

  val Component: FC[JsUnit] = define.fc[JsUnit] { p =>
    println(s"**** rendering UserInfo")

    val ctx = React.useContext(AAS.AuthContext)

    AAS.CheckLogin.props(
      CheckLoginProps(
        span.props(
          HTMLAttributes(
            style = new CSSProperties { marginRight = "20px" }
          ),
          greetings(ctx)
        ),
        ""
      )
    )
  }

  private def greetings(ctx: IAuthContext) =
    if (ctx == null ||
        js.isUndefined(ctx) ||
        ctx.auth == null ||
        js.isUndefined(ctx.auth) ||
        js.isUndefined(ctx.auth.merge[Auth].tokenParsed) ||
        js.isUndefined(ctx.auth.merge[Auth].tokenParsed.get()))
      ""
    else "Hello " + ctx.auth.merge[Auth].tokenParsed.get().asInstanceOf[js.Dynamic].preferred_username.asInstanceOf[String]
}
