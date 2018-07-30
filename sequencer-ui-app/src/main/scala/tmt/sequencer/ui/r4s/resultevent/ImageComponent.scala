package tmt.sequencer.ui.r4s.resultevent

import com.github.ahnfelt.react4s._
import org.scalajs.dom.raw.EventSource
import tmt.sequencer.ui.r4s.theme._

case class ImageComponent(client: P[EventSource]) extends Component[NoEmit] {

  val imageDataAsUrl: State[String] = State("")

  override def componentWillRender(get: Get): Unit = {
    if (get(imageDataAsUrl).isEmpty) {
      get(client).onmessage = { x =>
        val string = x.data.toString
        if (string.nonEmpty) {
          imageDataAsUrl.set(string)
        }
      }
    }
  }

  override def render(get: Get): ElementOrComponent = E.div(
    RightColumnCss,
    E.img(A.src(get(imageDataAsUrl)), A.width("200"), ResultTitleAreaCss)
  )

  override def componentWillUnmount(get: Get): Unit = {
    get(client).close()
  }
}
