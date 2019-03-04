package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.ui.r4s.theme._

case class HeaderComponent() extends Component[NoEmit] {

  val headerText = State("Sequencer component")

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
          Text(get(headerText))
        ),
        E.div(
          E.span(
            E.textarea(
              TextAreaCss,
              S.height.px(280),
              A.onChangeText(headerText.set),
              A.value(get(headerText))
            )
          )
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
