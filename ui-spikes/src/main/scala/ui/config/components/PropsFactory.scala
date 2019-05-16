package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents.{ButtonProps, TypographyProps}
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.PropTypesNs.Color
import typings.atMaterialDashUiCoreLib.fabFabMod.FabProps
import typings.atMaterialDashUiCoreLib.gridGridMod.{GridJustification, GridProps, GridSpacing}
import typings.atMaterialDashUiCoreLib.typographyTypographyMod
import typings.atMaterialDashUiCoreLib.typographyTypographyMod.Style
import typings.reactLib.reactMod.{CSSProperties, MouseEventHandler}
import typings.{atMaterialDashUiCoreLib, stdLib}

import scala.scalajs.js
import scala.scalajs.js.|

object PropsFactory {

  def typographyProps(
      _color: Color | textPrimary | textSecondary | error = null,
      _variant: js.UndefOr[Style | inherit] = js.undefined,
      _style: js.UndefOr[CSSProperties] = js.undefined
  ): TypographyProps = {
    val p = typographyTypographyMod.TypographyProps(color = _color)
    p.variant = _variant
    p.style = _style
    p
  }

  def buttonProps(
      _color: Color = null,
      _href: String = null,
      _size: js.UndefOr[small | medium | large] = js.undefined,
      _onClick: MouseEventHandler[stdLib.HTMLElement] = null
  ): ButtonProps = {
    val btnProps = ButtonProps(action = null, color = _color, href = _href, onClick = _onClick)
    btnProps.size = _size
    btnProps
  }

  def gridProps(
      _item: js.UndefOr[scala.Boolean] = js.undefined,
      _style: js.UndefOr[CSSProperties] = js.undefined,
      _spacing: js.UndefOr[GridSpacing] = js.undefined,
      _container: js.UndefOr[scala.Boolean] = js.undefined,
      _justify: GridJustification = null
  ): GridProps = {
    val grid = GridProps(
      container = _container,
      justify = _justify,
      item = _item
    )
    grid.style = _style
    grid.spacing = _spacing
    grid
  }

  def fabProps(
      _className: java.lang.String = null,
      _color: atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.PropTypesNs.Color = null,
      _style: scala.scalajs.js.UndefOr[typings.reactLib.reactMod.CSSProperties] = js.undefined
  ): FabProps = {
    val fab = FabProps(
      action = null,
      className = "fab",
      color = primary
    )
    fab.style = _style
    fab
  }

}
