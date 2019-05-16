package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents.SvgIconProps
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.{^ ⇒ Mui}
import typings.atMaterialDashUiIconsLib.atMaterialDashUiIconsMod.{^ ⇒ Icons}
import typings.csstypeLib.csstypeLibStrings
import typings.cswDashAasDashJsLib.cswDashAasDashJsLibComponents.ClientRoleProps
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.{^ ⇒ AAS}
import typings.reactLib.reactMod
import typings.reactLib.reactMod.{FC, HTMLAttributes}
import ui.config.components.PropsFactory.fabProps
import ui.config.context.contexts.Context.ModalOpenStore
import ui.todo.lib.JsUnit

object AddButton {
  import typings.reactLib.dsl._

  private val bottomCorner = new reactMod.CSSProperties {
    position = csstypeLibStrings.absolute
    bottom = "20px"
    right = "20px"
  }

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering AddButton")
    val (_, setModalOpen) = ModalOpenStore.use()

    AAS.ClientRole.props(
      ClientRoleProps(
        clientRole = "admin",
        error = span.props(
          HTMLAttributes(style = bottomCorner),
          "Please login as admin to add new configurations"
        ),
        client = "csw-config-server"
      ),
      Mui.Fab.props(
        fabProps(
          _className = "fab",
          _color = primary,
          _style = bottomCorner
        ),
        Icons.Add.props(SvgIconProps(onClick = _ ⇒ setModalOpen(true)))
      ),
      AddConfigModel.Component.noprops()
    )
  }
}
