package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.ui.r4s.theme._

case class OperationFrameComponent() extends Component[NoEmit] {
  override def render(get: Get): Node = {
    E.div(
      OperationTitleCss,
      Text("Sequence Feeder"),
      E.div(TextAreaCss, E.span(
        Text("E.pre(CodeCss, E.span(\n            " +
          "DefaultCodeCss,\n            " +
          "if(get(highlight)) Tags(find(get(code).trim))\n         " +
          "   else Text(get(code).trim)\n        ))")
      )

      ),
      E.button(ButtonCss,
        Text("Submit")
      )
    )
  }
}
