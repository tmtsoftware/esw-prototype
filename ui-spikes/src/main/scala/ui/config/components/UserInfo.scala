package ui.config.components

import typings.cswDashAasDashJsLib.cswDashAasDashJsLibComponents.{Consumer, ConsumerProps}
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.^.CheckLogin
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.{AuthContext, CheckLoginProps}
import typings.keycloakDashJsLib.keycloakDashJsMod.KeycloakProfile
import typings.reactLib.reactMod
import typings.reactLib.reactMod.{FC, HTMLAttributes}
import ui.todo.lib.JsUnit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object UserInfo {
  import typings.reactLib.dsl._

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering UserInfo")

    val props = ConsumerProps[AuthContext] { ctx ⇒
      val p = concurrent.Promise[String]()

      val cb: js.Function1[KeycloakProfile, Unit] = profile ⇒ p.success("Hello" + profile.username.get)

      if (js.isUndefined(ctx) ||
          js.isUndefined(ctx.auth) ||
          js.isUndefined(ctx.auth.loadUserProfile) ||
          ctx.auth.loadUserProfile.isEmpty) p.success("")
      else
        ctx.auth.loadUserProfile.get.success(cb)

      p.future.toJSPromise
    }

    CheckLogin.props(
      CheckLoginProps(
        span.props(
          HTMLAttributes(
            style = new reactMod.CSSProperties {
              marginRight = "20px"
            }
          ),
//          Consumer.props(props)
          "Hello xxx"
        ),
        "" // fixme: why this is required?
      )
    )
  }

}
