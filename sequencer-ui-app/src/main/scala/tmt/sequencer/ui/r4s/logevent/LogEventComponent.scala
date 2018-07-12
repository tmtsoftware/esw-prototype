package tmt.sequencer.ui.r4s.logevent

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss, TextAreaCss}

import scala.util.{Failure, Success}

case class LogEventComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {
  val logEventResponse = State("")
  val isLogVisibleS = State(false)

  import scala.concurrent.ExecutionContext.Implicits.global

  def handleShowLogs(get: Get): Unit = get(client).pause().onComplete {
    case Success(logResponse) => logEventResponse.set(logResponse.toString)
    case Failure(ex) => logEventResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    val isLogVisible       = get(isLogVisibleS)
    val buttonText = if(isLogVisible) "Hide Logs" else "Show Logs"
    E.div(
      OperationTitleCss,
      Text(s"Sequencer Logs"),
      E.div(
        E.span(
          E.textarea(
            TextAreaCss,
            S.height.px(280),
            A.value(get(logEventResponse))
          )
        )
      ),
      E.button(ButtonCss, Text(buttonText), A.onClick(e => {
        e.preventDefault()
        isLogVisibleS.set(!isLogVisible)
        handleShowLogs(get)
      }))
    )
  }
}
