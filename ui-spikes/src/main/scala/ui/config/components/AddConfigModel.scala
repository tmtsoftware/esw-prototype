package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents.Dialog
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.^._
import typings.atMaterialDashUiCoreLib.buttonButtonMod.ButtonProps
import typings.atMaterialDashUiCoreLib.dialogDialogMod.DialogProps
import typings.atMaterialDashUiCoreLib.dialogTitleDialogTitleMod.DialogTitleProps
import typings.atMaterialDashUiCoreLib.paperPaperMod.PaperProps
import typings.atMaterialDashUiCoreLib.textFieldTextFieldMod.TextFieldProps
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.{Auth, ^ ⇒ AAS}
import typings.reactLib.reactMod.{FC, ^ ⇒ React}
import typings.stdLib
import ui.config.ConfigClient
import ui.config.context.contexts.Context.{ConfigStore, ModalOpenStore}
import ui.config.models.Item
import ui.todo.lib.{GenericState, JsUnit}

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

object AddConfigModel {
  import typings.reactLib.dsl._

  val Component: FC[JsUnit] = define.fc[JsUnit] { _ =>
    println(s"**** rendering AddButton")
    val (modalOpen, setModalOpen) = ModalOpenStore.use()
    val ctx                       = React.useContext(AAS.AuthContext)
    val (items, setItems)         = ConfigStore.use()
    val (path, setPath)           = GenericState.use("")
    val (commitMsg, setCommitMsg) = GenericState.use("")
    val (content, setContent)     = GenericState.use("")

    def clearData(): Unit         = { setPath(""); setCommitMsg(""); setContent("") }
    def closeAddModal(): Unit     = setModalOpen(false)
    def clearDataAndClose(): Unit = { clearData(); closeAddModal() }

    def saveConfig(): Unit = async {
      await(ConfigClient.addConfig(path, commitMsg, ctx.auth.merge[Auth].token.get().get, commitMsg))
      val username =
        if (ctx.auth != null && !js.isUndefined(ctx.auth))
          ctx.auth.merge[Auth].tokenParsed.get().asInstanceOf[js.Dynamic].preferred_username.asInstanceOf[String]
        else "unknown"

      setItems(items :+ Item(path, path, username, commitMsg))
      clearDataAndClose()
    }

    val dialogProps = js.Dynamic
      .literal(
        fullWidth = true,
        open = modalOpen,
        onClose = () ⇒ clearDataAndClose(),
        PaperProps = PaperProps(`aria-labelledby` = "form-dialog-title").asInstanceOf[stdLib.Partial[PaperProps]]
      )
      .asInstanceOf[DialogProps]

    val configTxtProps = TextFieldProps(
      margin = dense,
      id = "config_text",
      label = "Configuration Text",
      datatype = "text",
      itemType = "text",
      fullWidth = true,
      defaultValue = content,
      multiline = true,
      onChange = e ⇒ setContent(e.target.asInstanceOf[stdLib.HTMLTextAreaElement].value)
    )
    configTxtProps.rows = 4
    configTxtProps.variant = outlined

    Dialog.props(
      dialogProps,
      DialogTitle.props(
        DialogTitleProps(id = "orm-dialog-title"),
        "Add new configuration"
      ),
      DialogContent.noprops(
        TextField.props(
          TextFieldProps(
            autoFocus = true,
            margin = dense,
            id = "path",
            label = "Path",
            datatype = "text",
            itemType = "text",
            fullWidth = true,
            defaultValue = path,
            multiline = false,
            onChange = e ⇒ setPath(e.target.asInstanceOf[stdLib.HTMLTextAreaElement].value)
          )
        ),
        TextField.props(
          TextFieldProps(
            margin = dense,
            id = "commit_message",
            label = "Commit Message",
            datatype = "text",
            itemType = "text",
            fullWidth = true,
            defaultValue = commitMsg,
            multiline = true,
            onChange = e ⇒ setCommitMsg(e.target.asInstanceOf[stdLib.HTMLTextAreaElement].value)
          )
        ),
        TextField.props(configTxtProps)
      ),
      DialogActions.noprops(
        Button.props(
          ButtonProps(
            action = null,
            onClick = _ ⇒ clearDataAndClose(),
            color = secondary
          ),
          "Cancel"
        ),
        Button.props(
          ButtonProps(
            action = null,
            onClick = _ ⇒ saveConfig(),
            color = primary
          ),
          "Save"
        )
      )
    )
  }
}
