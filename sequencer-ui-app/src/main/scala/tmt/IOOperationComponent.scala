package tmt

import com.github.ahnfelt.react4s._
import tmt.IOOperationComponent.HandleClick
import tmt.theme.{ButtonCss, OperationTitleCss, TextAreaCss}

case class IOOperationComponent(componentNameProp: P[String], operation: P[String], output: P[String])
    extends Component[IOOperationComponent.Msg] {

  val input = State("")

  override def render(get: Get): Node = {
    E.div(
      E.div(
        OperationTitleCss,
        Text(s"${get(componentNameProp)} Request"),
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
          E.button(
            ButtonCss,
            Text(get(operation)),
            A.onClick { e =>
              e.preventDefault()
              emit(HandleClick(get(input)))
            }
          )
        )
      ),
      E.div(
        OperationTitleCss,
        Text(s"${get(componentNameProp)} Response"),
        E.div(
          TextAreaCss,
          E.span(E.pre(Text(get(output))))
        )
      )
    )
  }
}

object IOOperationComponent {
  sealed trait Msg
  case class HandleClick(request: String) extends Msg
}
