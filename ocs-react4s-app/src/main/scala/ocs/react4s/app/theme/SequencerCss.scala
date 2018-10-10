package ocs.react4s.app.theme

import com.github.ahnfelt.react4s._

object RightColumnCss
    extends CssClass(
      S.left.percent(50),
      S.bottom.px(1703),
      S.padding.px(20),
      S.fontFamily("Verdana"),
      S.fontSize.px(16),
      S.color(Palette.text),
      S.overflowX("auto")
    )

object RightTitleCss
    extends CssClass(
      S.position.fixed()
    )

object TextAreaCss
    extends CssClass(
      S.position.relative(),
      S.boxSizing.borderBox(),
      S.backgroundColor(Palette.background),
      S.color(Palette.text),
      S.top.px(12),
      S.boxShadow("0 1px 2px rgba(0, 0, 0, 0.3)"),
      S.margin.px(28),
      S.padding.px(10),
      S.fontFamily("monospace"),
      S.fontSize.px(14),
      S.overflow("auto"),
      S.height.px(300),
      S.width.percent(90)
    )

object ResultTextAreaCss
    extends CssClass(
      TextAreaCss,
      S.position.fixed(),
      S.left("52%"),
      S.top.px(97),
      S.height.px(430),
      S.width.percent(50)
    )

object ButtonCss
    extends CssClass(
      S.fontSize.px(16),
      S.borderColor(Palette.grey),
      S.borderRadius.px(3),
    )

object ListComponentCss
    extends CssClass(
      S.marginTop.percent(5),
      S.marginBottom.percent(5),
      S.width.percent(70)
    )
