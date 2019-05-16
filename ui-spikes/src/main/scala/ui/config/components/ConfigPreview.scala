package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.cardCardMod.CardProps
import typings.atMaterialDashUiIconsLib.atMaterialDashUiIconsMod.{^ ⇒ Icons}
import typings.cswDashAasDashJsLib.cswDashAasDashJsLibComponents.ClientRoleProps
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.{Auth, ^ ⇒ AAS}
import typings.reactLib.reactMod._
import ui.config.ConfigClient
import ui.config.context.contexts.Context.ConfigStore
import ui.config.models.Item

import scala.scalajs.js

object ConfigPreview {

  import PropsFactory._
  import typings.reactLib.dsl._

  private val cardStyle = new CSSProperties {
    minWidth = 275
  }
  private val typographyStyle = new CSSProperties {
    marginBottom = 12
  }

  class ConfigPreviewProps(val item: Item) extends js.Object
  object ConfigPreviewProps {
    def apply(item: Item): ConfigPreviewProps = new ConfigPreviewProps(item)
  }

  val Component: FC[ConfigPreviewProps] = define.fc[ConfigPreviewProps] { props =>
    println(s"**** rendering ConfigPreview")
    val item = props.item
    val ctx = ^.useContext(cswDashAasDashJsMod.^.AuthContext)

    val (items, setItems) = ConfigStore.use()

    Card.props(
      CardProps(style = cardStyle),
      CardContent.noprops(
        Typography.props(
          typographyProps(_variant = atMaterialDashUiCoreLibStrings.h5),
          item.path
        ),
        Typography.props(
          typographyProps(_style = typographyStyle, _color = textSecondary),
          item.author
        )
      ),
      CardActions.noprops(
        Button.props(
          buttonProps(
            _color = primary,
            _href = s"http://localhost:5000/config/${item.path}",
            _size = atMaterialDashUiCoreLibStrings.small,
          ),
          Icons.CloudDownloadRounded.noprops(),
          "Download"
        ),
        AAS.ClientRole.props(
          ClientRoleProps(
            clientRole = "admin",
            error = "",
            client = "csw-config-server"
          ),
          Button.props(
            buttonProps(
              _color = secondary,
              _onClick = _ ⇒ {
                val token = ctx.auth.merge[Auth].token.get().get
                ConfigClient.delete(item.path, token)
                setItems(items.filter(_ != item))
              }
            ),
            Icons.Delete.noprops(),
            "Delete"
          )
        )
      )
    )
  }
}
