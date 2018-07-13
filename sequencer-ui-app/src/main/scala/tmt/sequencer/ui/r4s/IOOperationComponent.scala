package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.ui.r4s.IOOperationComponent.HandleClick
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss, TextAreaCss}

case class IOOperationComponent(componentNameProp: P[String], operation: P[String], output: P[String])
    extends Component[IOOperationComponent.Msg] {
  val input = State("")

  override def render(get: Get): Node = {
    val componentName = get(componentNameProp)
    E.div(
      E.div(
        OperationTitleCss,
        Text(s"$componentName Request"),
        E.div(
          E.span(
            E.textarea(
              TextAreaCss,
              S.height.px(280),
              A.onChangeText(input.set),
              A.value(get(input))
            )
          )
        ),
        E.div(
          E.button(ButtonCss, Text(get(operation)), A.onClick(e => {
            e.preventDefault()
            emit(HandleClick(get(input)))
          }))
        )
      ),
      E.div(
        OperationTitleCss,
        Text(s"$componentName Response"),
        E.div(
          TextAreaCss,
          E.span(
            E.pre(
              Text(
                get(output)
              )
            )
          )
        )
      )
    )
  }
}

object IOOperationComponent {
  sealed trait Msg
  case class HandleClick(request: String) extends Msg
}
