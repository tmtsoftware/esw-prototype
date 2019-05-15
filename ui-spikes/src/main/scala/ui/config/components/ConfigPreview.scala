package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.cardCardMod.CardProps
import typings.atMaterialDashUiCoreLib.{atMaterialDashUiCoreLibStrings, typographyTypographyMod}
import typings.atMaterialDashUiIconsLib.atMaterialDashUiIconsMod.{^ ⇒ Icons}
import typings.cswDashAasDashJsLib.cswDashAasDashJsLibComponents.ClientRoleProps
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.{AuthContext, ^ ⇒ AAS}
import typings.reactLib.dsl.{div, _}
import typings.reactLib.reactMod
import typings.reactLib.reactMod._
import ui.config.ConfigClient
import ui.config.models.Item

import scala.scalajs.js

object ConfigPreview {

  private val cardStyle = new reactMod.CSSProperties {
    minWidth = 275
  }
  private val ts = new CSSProperties {
    marginBottom = 12
  }

  class ConfigPreviewProps(val item: Item) extends js.Object
  object ConfigPreviewProps {
    def apply(item: Item): ConfigPreviewProps = new ConfigPreviewProps(item)
  }

  val Component: FC[ConfigPreviewProps] = define.fc[ConfigPreviewProps] { props =>
    val item = props.item

    def deleteComp(path: String): ReactElement[ConsumerProps[AuthContext]] = {
      val props = ConsumerProps[AuthContext] { ctx ⇒
        div.props(
          HTMLAttributes(
            onClick = _ ⇒ {
              println("Deleting .... ")
              ConfigClient.delete(path, ctx.auth.token.get())
            }
          ),
          "Delete"
        )
      }

      AAS.Consumer.props(props)
    }

    Card.props(
      CardProps(style = cardStyle),
      CardContent.noprops(
        Typography.props(
          typographyTypographyMod.TypographyProps(
            variant = atMaterialDashUiCoreLibStrings.h5
          ),
          item.path
        ),
        Typography.props(
          typographyTypographyMod.TypographyProps(
            style = ts,
            color = textSecondary
          ),
          item.author
        )
      ),
      CardActions.noprops(
        Button.props(
          ButtonProps(
            color = primary,
            size = atMaterialDashUiCoreLibStrings.small,
            href = s"http://localhost:5000/config/${item.path}"
          ),
          Icons.CloudDownloadRounded.noprops(),
          div.props(
            HTMLAttributes(
              onClick = _ ⇒ {
                println("Downloading ...")
              }
            ),
            "Download"
          )
        ),
        AAS.ClientRole.props(
          ClientRoleProps(
            clientRole = "admin",
            error = "",
            client = "csw-config-server"
          ),
          Button.props(
            ButtonProps(
              color = secondary
            ),
            Icons.Delete.noprops(),
            deleteComp(item.path)
          )
        )
      )
    )
  }
}
