package tmt
import com.github.ahnfelt.react4s._
import tmt.theme.{AnchorCss, BrandTaglineCss, BrandTitleCss, TopBarCss}

case class HeaderComponent() extends Component[NoEmit] {
  override def render(get: Get): Node = {
    E.div(
      E.div(
        TopBarCss,
        E.span(
          BrandTitleCss,
          Text("TMT")
        ),
        E.span(
          BrandTaglineCss,
          Text("Sequencer component")
        )
      ),
      E.a(
        AnchorCss,
        A.href("https://github.com/tmtsoftware/esw-prototype"),
        Text("Github")
      )
    )
  }
}
