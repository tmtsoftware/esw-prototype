package tmt.sequencer.ui.r4s.resultevent

import com.github.ahnfelt.react4s._
import org.scalajs.dom.raw.EventSource
import tmt.sequencer.ui.r4s.theme._

case class ResultEventComponent(client: P[EventSource]) extends Component[NoEmit] {

  val streamDataListS: State[List[String]] = State(List.empty[String])

  override def componentWillRender(get: Get): Unit = {
    if (get(streamDataListS).isEmpty) {
      get(client).onmessage = { x =>
        streamDataListS.set(get(streamDataListS) :+ s"${x.data.toString}\n")
      }
    }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      RightColumnCss,
      E.p(
        ResultTitleAreaCss,
        Text("Server Result Stream")
      ),
      E.ul(
        ResultTextAreaCss,
        Tags(get(streamDataListS).map(stream => E.li(Text(stream))))
      )
    )
  }

  override def componentWillUnmount(get: Get): Unit = {
    get(client).close()
  }
}
