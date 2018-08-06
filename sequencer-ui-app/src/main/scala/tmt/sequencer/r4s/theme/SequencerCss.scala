package tmt.sequencer.r4s.theme

import com.github.ahnfelt.react4s._

object OperationTitleCss
    extends CssClass(
      S.position.relative(),
      S.left.px(0),
      S.top.px(50),
      S.padding.px(20),
      S.fontFamily("Verdana"),
      S.fontSize.px(16),
      S.color(Palette.text),
    )

object RightColumnCss
    extends CssClass(
      S.left.percent(50),
      S.bottom.px(1703),
      S.padding.px(20),
      S.fontFamily("Verdana"),
      S.fontSize.px(16),
      S.color(Palette.text),
      S.overflowX("auto"),
    )

object RightButtonCss
    extends CssClass(
      ButtonCss,
      S.fontFamily("Verdana"),
      S.position.fixed(),
      S.top.px(80),
      S.right.px(8),
    )

object TextAreaCss
    extends CssClass(
      S.position.relative(),
      S.boxSizing.borderBox(),
      S.backgroundColor(Palette.background),
      S.color(Palette.text),
      S.top.px(12),
      S.left.px(-10),
      S.width.percent(50),
      S.boxShadow("0 1px 2px rgba(0, 0, 0, 0.3)"),
      S.margin.px(10),
      S.padding.px(10),
      S.fontFamily("monospace"),
      S.fontSize.px(14),
      S.overflow("auto"),
      S.height.px(300)
    )

object ResultTextAreaCss
    extends CssClass(
      TextAreaCss,
      S.position.fixed(),
      S.left("52%"),
      S.top.px(97),
      S.height.px(430),
      S.overflow("auto")
    )

object ResultTitleAreaCss
    extends CssClass(
      OperationTitleCss,
      S.position.fixed(),
      S.left("52%"),
      S.top.px(55)
    )

object TopBarCss
    extends CssClass(
      S.borderTop("5px solid " + Palette.blue),
      S.backgroundColor(Palette.background),
      S.boxShadow("0 2px 5px rgba(0, 0, 0, 0.3)"),
      S.boxSizing.borderBox(),
      S.position.absolute(),
      S.left.px(0),
      S.top.px(0),
      S.right.px(0),
      S.height.px(50),
    )

object BrandTextCss
    extends CssClass(
      S.display.inlineBlock(),
      S.paddingTop.px(8),
      S.fontFamily("Verdana")
    )

object BrandTitleCss
    extends CssClass(
      BrandTextCss,
      S.color(Palette.blue),
      S.paddingLeft.px(50),
      S.fontSize.px(20),
    )

object BrandTaglineCss
    extends CssClass(
      BrandTextCss,
      S.paddingLeft.px(50),
    )

object ButtonCss
    extends CssClass(
      S.fontSize.px(16),
      S.width.percent(10),
      S.color(Palette.blue),
      S.borderColor(Palette.grey)
    )

object AnchorCss
    extends CssClass(
      LinkCss,
      S.fontFamily("Verdana"),
      S.fontSize.px(16),
      S.position("absolute"),
      S.top.px(15),
      S.right.px(10),
    )
