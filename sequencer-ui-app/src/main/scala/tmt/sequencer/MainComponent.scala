package tmt.sequencer

import com.github.ahnfelt.react4s._
import org.scalajs.dom
import tmt.Path

case class MainComponent() extends Component[NoEmit] {
  val page = State(Routes.router.data(Path.hashPath))

  dom.window.onhashchange = { _ =>
    page.set(Routes.router.data(Path.hashPath))
  }

  override def render(get: Get): Node = {
    E.div(
      get(page)
        .map(Component(PageComponent, _))
        .getOrElse(E.div(Text("Not found")))
    )
  }
}
