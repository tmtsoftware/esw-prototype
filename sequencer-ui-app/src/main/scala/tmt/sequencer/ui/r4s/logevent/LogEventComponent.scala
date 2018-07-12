package tmt.sequencer.ui.r4s.logevent

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceLoggerClient
import tmt.sequencer.ui.r4s.theme.{OperationTitleCss, TextAreaCss}

case class LogEventComponent() extends Component[NoEmit] {
  val streamData: State[String] = State("")
  val HOST_IP                   = "10.131.23.146"

  import scala.concurrent.ExecutionContext.Implicits.global

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
