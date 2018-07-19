package tmt.sequencer.ui.r4s.theme

import com.github.ahnfelt.react4s._

object LinkCss
    extends CssClass(
      S.color(Palette.blue),
      S.textDecoration.none(),
      S.cursor.pointer(),
      Css.hover(S.textDecoration("underline"))
    )
