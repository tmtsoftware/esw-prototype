package tmt.sequencer.ui.r4s.logevent

import com.github.ahnfelt.react4s._
import org.scalajs.dom.raw.EventSource
import tmt.sequencer.ui.r4s.SequencerConstants
import tmt.sequencer.ui.r4s.theme._

case class LogEventComponent(client: P[EventSource]) extends Component[NoEmit] {
  val streamDataListS: State[List[String]] = State(List.empty[String])

  override def componentWillRender(get: Get): Unit = {
    if (get(streamDataListS).isEmpty) {
      get(client).onmessage = { x =>
        streamDataListS.set(get(streamDataListS) :+ s"$x\n")
      }
    }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      RightColumnCss,
      E.p(LogTitleAreaCss, Text(SequencerConstants.SERVER_LOG_STREAM)),
      E.ul(LogTextAreaCss, Tags(get(streamDataListS).map { stream =>
        E.li(Text(stream))
      }))
    )
  }

  override def componentWillUnmount(get: Get): Unit = {
    get(client).close()
  }
}
