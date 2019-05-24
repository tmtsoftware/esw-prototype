package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents.SvgIconProps
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.{^ ⇒ Mui}
import typings.atMaterialDashUiCoreLib.stylesCreateMuiThemeMod.Theme
import typings.atMaterialDashUiCoreLib.stylesWithStylesMod.CSSProperties
import typings.atMaterialDashUiIconsLib.atMaterialDashUiIconsMod.{^ ⇒ Icons}
import typings.csstypeLib.csstypeLibStrings
import typings.cswDashAasDashJsLib.cswDashAasDashJsLibComponents.ClientRoleProps
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.ClientRole
import typings.reactLib.reactMod.{FC, HTMLAttributes}
import ui.config.components.PropsFactory.fabProps
import ui.config.components.utils.StyledFC
import ui.config.context.contexts.Context.ModalOpenStore
import ui.todo.lib.JsUnit

import scala.scalajs.js

object AddButton {
  import typings.reactLib.dsl._

  trait StyleOverrides[T] extends js.Object {
    val fab: T
  }

  val styles: js.Function1[Theme, StyleOverrides[CSSProperties]] = _ =>
    new StyleOverrides[CSSProperties] {
      override val fab: CSSProperties = new CSSProperties {
        position = csstypeLibStrings.absolute
        bottom = "20px"
        right = "20px"

      }
  }

  val Component: FC[JsUnit] = StyledFC[StyleOverrides, JsUnit](styles) { props =>
    println(s"**** rendering AddButton")
    val (_, setModalOpen) = ModalOpenStore.use()

    ClientRole(
      ClientRoleProps(
        clientRole = "admin",
        error = span.props(
          HTMLAttributes(className = props.classes.fab),
          "Please login as admin to add new configurations"
        ),
        client = "csw-config-server",
        children = js.Array(
          Mui.Fab.props(
            fabProps(
              _className = props.classes.fab,
              _color = primary,
            ),
            Icons.Add.props(SvgIconProps(onClick = _ ⇒ setModalOpen(true)))
          ),
          AddConfigModel.Component.noprops()
        )
      )
    )
  }
}
