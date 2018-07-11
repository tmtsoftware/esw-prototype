package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.ui.r4s.theme.{BrandTaglineCss, BrandTitleCss, LinkCss, TopBarCss}

case class HeaderComponent() extends Component[NoEmit] {
  override def render(get: Get): Node = {
    E.div(
      E.div(
        TopBarCss,
        E.span(Text("TMT"), BrandTitleCss),
        E.span(Text("Sequencer component"), BrandTaglineCss)
      ),
      E.a(
        S.fontFamily("Verdana"),
        S.fontSize.px(16),
        LinkCss,
        A.href("https://github.com/tmtsoftware/esw-prototype"),
        S.position("absolute"),
        S.top.px(15),
        S.right.px(10),
        Text("Github")
      )
    )
  }
}
