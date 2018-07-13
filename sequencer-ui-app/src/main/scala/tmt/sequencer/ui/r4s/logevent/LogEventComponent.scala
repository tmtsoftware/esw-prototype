package tmt.sequencer.ui.r4s.logevent

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceLoggerClient
import tmt.sequencer.ui.r4s.theme._

case class LogEventComponent(client: P[SequenceLoggerClient]) extends Component[NoEmit] {
  val streamData: State[String] = State("")
  val showLogsS                 = State(false)

  override def componentWillRender(get: Get): Unit = {
    if (get(streamData).isEmpty) {
      get(client).onLogEvent(x => {
        streamData.modify(_.concat(s"$x\n"))
      })
    }
  }

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

  override def componentWillUnmount(get: Get): Unit = {
    get(client).closeEventSource()
  }
}
