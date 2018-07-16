package tmt.sequencer.ui.r4s.logevent

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceLoggerClient
import tmt.sequencer.ui.r4s.SequencerConstants
import tmt.sequencer.ui.r4s.theme._

case class LogEventComponent(client: P[SequenceLoggerClient]) extends Component[NoEmit] {
  val streamDataListS: State[List[String]] = State(List.empty[String])
  val showLogsS                            = State(false)

  override def componentWillRender(get: Get): Unit = {
    if (get(streamDataListS).isEmpty) {
      get(client).onLogEvent(x => {
        streamDataListS.set(get(streamDataListS) :+ s"$x\n")
      })
    }
  }

  override def render(get: Get): ElementOrComponent = {
    val showLogs   = get(showLogsS)
    val buttonText = if (showLogs) SequencerConstants.HIDE_LOGS else SequencerConstants.SHOW_LOGS

    val logOutputText = if (showLogs) {
      E.ul(LogTextAreaCss, Tags(get(streamDataListS).map { stream =>
        E.li(Text(stream))
      }))
    } else {
      E.ul()
    }

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
