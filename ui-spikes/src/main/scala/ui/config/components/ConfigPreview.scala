package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.cardCardMod.CardProps
import typings.atMaterialDashUiCoreLib.stylesCreateMuiThemeMod.Theme
import typings.atMaterialDashUiCoreLib.stylesWithStylesMod.CSSProperties
import typings.atMaterialDashUiIconsLib.atMaterialDashUiIconsMod.{^ ⇒ Icons}
import typings.cswDashAasDashJsLib.cswDashAasDashJsLibComponents.ClientRoleProps
import typings.cswDashAasDashJsLib.cswDashAasDashJsMod.ClientRole
import typings.cswDashAasDashJsLib.distComponentsAuthMod.Auth
import typings.cswDashAasDashJsLib.{cswDashAasDashJsMod ⇒ AAS}
import typings.reactLib.reactMod.{^ ⇒ React}
import typings.reactLib.reactMod._
import ui.config.ConfigClient
import ui.config.components.utils.StyledFC
import ui.config.context.contexts.Context.ConfigStore
import ui.config.models.Item

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

object ConfigPreview {

  import PropsFactory._
  import typings.reactLib.dsl._

  trait StyleOverrides[T] extends js.Object {
    val card: T
    val bullet: T
    val title: T
    val pos: T
  }

  val styles: js.Function1[Theme, StyleOverrides[CSSProperties]] = _ =>
    new StyleOverrides[CSSProperties] {
      override val card: CSSProperties = new CSSProperties {
        minWidth = 275
      }

      override val bullet: CSSProperties = new CSSProperties {
        display = "inline-block"
        margin = "0 2px"
        transform = "scale(0.8)"
      }

      override val title: CSSProperties = new CSSProperties {
        fontSize = 14
      }

      override val pos: CSSProperties = new CSSProperties {
        marginBottom = 12
      }
  }

  class ConfigPreviewProps(val item: Item) extends js.Object
  object ConfigPreviewProps {
    def apply(item: Item): ConfigPreviewProps = new ConfigPreviewProps(item)
  }

  val Component: FC[ConfigPreviewProps] = StyledFC[StyleOverrides, ConfigPreviewProps](styles) { props =>
    println(s"**** rendering ConfigPreview")
    val item = props.item
    val ctx  = React.useContext(AAS.AuthContext)

    val (items, setItems) = ConfigStore.use()

    Card.props(
      CardProps(props.classes.card),
      CardContent.noprops(
        Typography.props(
          typographyProps(_variant = atMaterialDashUiCoreLibStrings.h5),
          item.path
        ),
        Typography.props(
          typographyProps(_className = props.classes.pos, _color = textSecondary),
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
        ClientRole(
          ClientRoleProps(
            clientRole = "admin",
            error = "",
            client = "csw-config-server",
            children = js.Array(
              Button.props(
                buttonProps(
                  _color = secondary,
                  _onClick = _ ⇒ {
                    val token = ctx.auth.merge[Auth].token.get
                    ConfigClient.delete(item.path, token)
                      .foreach(r ⇒ if(r.status == 200) setItems(items.filter(_ != item)))
                  }
                ),
                Icons.Delete.noprops(),
                "Delete"
              )
            )
          )
        )
      )
    )
  }
}
