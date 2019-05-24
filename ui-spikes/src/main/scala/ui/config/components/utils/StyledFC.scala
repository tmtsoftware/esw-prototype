package ui.config.components.utils

import typings.atMaterialDashUiCoreLib.stylesCreateMuiThemeMod.Theme
import typings.atMaterialDashUiCoreLib.stylesWithStylesMod.CSSProperties
import typings.reactLib.Anon_Children
import typings.reactLib.reactMod.{FC, ReactNode}

import scala.scalajs.js
import scala.scalajs.js.|

/* A facade to define functional components making use of `withStyles` */
object StyledFC {
  import scala.language.higherKinds

  @inline private def stylesMod = typings.atMaterialDashUiCoreLib.stylesMod.^.asInstanceOf[js.Dynamic]

  trait GeneratedClassNames[Styles[_] <: js.Object] extends js.Object {
    val classes: Styles[String]
  }

  @inline def apply[Styles[_] <: js.Object, P <: js.Object](
      styles: Styles[CSSProperties] | js.Function1[Theme, Styles[CSSProperties]]
  )(f: js.Function1[P with Anon_Children with GeneratedClassNames[Styles], ReactNode]): FC[P] =
    stylesMod.withStyles(styles.asInstanceOf[js.Any])(f).asInstanceOf[FC[P]]
}
