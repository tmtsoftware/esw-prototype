package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings.{bottom, inherit, left}
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.{^ ⇒ Mui}
import typings.atMaterialDashUiCoreLib.iconButtonIconButtonMod.IconButtonProps
import typings.atMaterialDashUiCoreLib.snackbarSnackbarMod.{SnackbarOrigin, SnackbarProps}
import typings.reactLib.reactMod.{FC, HTMLAttributes}
import ui.config.context.contexts.Context.{ErrorStore, ModalOpenStore}
import ui.todo.lib.JsUnit

object AppSnackbar {
  import typings.reactLib.dsl._

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering AppSnackbar")
    val (modalOpen, setModalOpen) = ModalOpenStore.use()
    val (error, setError)         = ErrorStore.use()

    Mui.Snackbar.props(
      SnackbarProps(
        open = error.isEmpty,
        onClose = (_, _) ⇒ setError(" "),
        anchorOrigin = SnackbarOrigin(
          horizontal = left,
          vertical = bottom
        ),
        message = span.props(HTMLAttributes(id = "message-id"), error.toString),
        action = Mui.IconButton.props(iconProps)
      )
    )
  }

  private def iconProps = {
    val i = IconButtonProps(action = null)
    i.color = inherit
    i
  }
}
