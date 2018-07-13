package tmt.sequencer.ui.r4s.logevent

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceLoggerClient
import tmt.sequencer.ui.r4s.theme._

case class LogEventComponent() extends Component[NoEmit] {
  val streamData: State[String] = State("")
  val HOST_IP                   = "10.131.23.146"
  val showLogsS                 = State(false)

  import scala.concurrent.ExecutionContext.Implicits.global

  private val sequenceLoggerClient = new SequenceLoggerClient(s"http://$HOST_IP:8000")

  sequenceLoggerClient.onLogEvent(x => streamData.set(x))

  override def render(get: Get): ElementOrComponent = {
    val showLogs   = get(showLogsS)
    val buttonText = if (showLogs) "Hide Logs" else "Show Logs"
    val logOutputText: ElementOrComponent =
      if (showLogs)
        E.textarea(
          TextAreaCss,
          Text(get(streamData))
        )
      else E.div()

    E.div(
      RightColumnCss,
      E.button(RightButtonCss, Text(buttonText), A.onClick(e => {
        e.preventDefault()
        showLogsS.set(!showLogs)
      })),
      logOutputText
    )
  }
}
