package ocs.react4s.app.sequencer.resultevent

import com.github.ahnfelt.react4s._
import ocs.react4s.app.theme.{ResultTextAreaCss, RightColumnCss, RightTitleCss}
import org.scalajs.dom.raw.EventSource

case class ResultEventComponent(client: P[EventSource]) extends Component[NoEmit] {

  val streamDataListS: State[List[String]] = State(List.empty[String])

  override def componentWillRender(get: Get): Unit = {
    if (get(streamDataListS).isEmpty) {
      get(client).onmessage = { x =>
        //TODO: Delimiter for different msgs
        if (x.data.toString.nonEmpty) {
          streamDataListS.set(get(streamDataListS) :+ s"${x.data.toString}\n${"*" * 50}\n")
        }
      }
    }
  }

  override def render(get: Get): ElementOrComponent = E.div(
    E.h6(
      RightTitleCss,
      Text("Sequencer Result Stream")
    ),
    E.div(
      RightColumnCss,
      E.ul(
        ResultTextAreaCss,
        Tags(get(streamDataListS).map(stream => E.li(Text(stream))))
      )
    )
  )

  override def componentWillUnmount(get: Get): Unit = {
    get(client).close()
  }
}
