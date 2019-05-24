package ui.config.components

import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibComponents.ButtonProps
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreLibStrings._
import typings.atMaterialDashUiCoreLib.atMaterialDashUiCoreMod.PropTypesNs.Color
import typings.atMaterialDashUiCoreLib.fabFabMod.FabProps
import typings.atMaterialDashUiCoreLib.gridGridMod.{GridJustification, GridProps, GridSpacing}
import typings.atMaterialDashUiCoreLib.typographyTypographyMod
import typings.atMaterialDashUiCoreLib.typographyTypographyMod.{Style, TypographyProps}
import typings.reactLib.reactMod.{CSSProperties, MouseEventHandler}
import typings.{atMaterialDashUiCoreLib, stdLib}

import scala.scalajs.js
import scala.scalajs.js.|

object PropsFactory {

  def typographyProps(
      _color: Color | textPrimary | textSecondary | error = null,
      _variant: js.UndefOr[Style | inherit] = js.undefined,
      _style: js.UndefOr[CSSProperties] = js.undefined,
      _className: java.lang.String = null
  ): TypographyProps = {
    val p = typographyTypographyMod.TypographyProps(className = _className, color = _color)
    p.variant = _variant
    p.style = _style
    p
  }

  def buttonProps(
      _color: Color = null,
      _href: String = null,
      _size: js.UndefOr[small | medium | large] = js.undefined,
      _onClick: MouseEventHandler[stdLib.HTMLElement] = null,
      _variant: js.UndefOr[text | flat | outlined | contained | raised | fab | extendedFab] = js.undefined,
      _style: scala.scalajs.js.UndefOr[typings.reactLib.reactMod.CSSProperties] = js.undefined
  ): ButtonProps = {
    val btnProps = ButtonProps(action = null, color = _color, href = _href, onClick = _onClick)
    btnProps.size = _size
    btnProps.variant = _variant
    btnProps.style = _style
    btnProps
  }

  def gridProps(
      _className: java.lang.String = null,
      _item: js.UndefOr[scala.Boolean] = js.undefined,
      _style: js.UndefOr[CSSProperties] = js.undefined,
      _spacing: js.UndefOr[GridSpacing] = js.undefined,
      _container: js.UndefOr[scala.Boolean] = js.undefined,
      _justify: GridJustification = null
  ): GridProps = {
    val grid = GridProps(
      container = _container,
      justify = _justify,
      item = _item,
      className = _className
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
      className = _className,
      color = primary
    )
    fab.style = _style
    fab
  }

}
