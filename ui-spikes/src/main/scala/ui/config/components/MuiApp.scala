package ui.config.components

import typings.cswDashAasDashJsLib.{Anon_ClientId, cswDashAasDashJsLibComponents}
import typings.cswDashAasDashJsLib.cswDashAasDashJsLibComponents.{AuthContextProvider, AuthContextProviderProps}
import typings.reactDashDomLib.reactDashDomMod.{^ ⇒ ReactDom}
import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC
import typings.stdLib.{Element, ^}
import ui.config.context.contexts
import ui.todo.lib.JsUnit

object MuiApp {

  def render(): Unit = {
    ReactDom.render(
      MuiApp.App.noprops(),
      ^.document.getElementById("todo").asInstanceOf[Element]
    )
  }

  private val AuthProvider: FC[JsUnit] = define.fc[JsUnit] { props =>
    AuthContextProvider.props(
      AuthContextProviderProps(props.children.getOrElse(null), Anon_ClientId("csw-config-app", "TMT"))
    )
  }

  private val App = define.fc[JsUnit] { _ =>
    println(s"**** rendering MuiApp")
    AuthProvider.noprops(
      contexts.Context.Provider.noprops(
          ConfigApp.Component.noprops()
        )
      )
  }
}
