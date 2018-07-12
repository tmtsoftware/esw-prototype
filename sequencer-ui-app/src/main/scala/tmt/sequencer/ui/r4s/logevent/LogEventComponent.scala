package tmt.sequencer.ui.r4s.logevent

import com.github.ahnfelt.react4s.{E, _}
import org.scalajs.dom.EventSource
import tmt.sequencer.SequenceLoggerClient
import tmt.sequencer.ui.r4s.theme.{OperationTitleCss, TextAreaCss}

case class LogEventComponent() extends Component[NoEmit] {
  val streamData: State[String] = State("")
  val HOST_IP                   = "10.131.23.146"
  /*
    Start `event-monitor-server` on local box, give host address as shown below.
    localhost or broadcast address is not acceptable due to CORS error in the browser
   */

  private val sequenceLoggerClient = new SequenceLoggerClient(s"http://$HOST_IP:8000")

  sequenceLoggerClient.onLogEvent(x => streamData.set(x))

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      E.pre(
        TextAreaCss,
        Text(get(streamData))
      )
    )
  }
}
