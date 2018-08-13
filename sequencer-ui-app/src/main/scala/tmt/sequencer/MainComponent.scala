package tmt.sequencer

import com.github.ahnfelt.react4s._
import org.scalajs.dom
import tmt.sequencer.r4s.HeaderComponent

case class MainComponent() extends Component[NoEmit] {
  private def hashPath: String = dom.window.location.hash.drop(1)

  val page = State(Routes.router.data(hashPath))

  dom.window.onhashchange = { _ =>
    page.set(Routes.router.data(hashPath))
  }

  override def render(get: Get): Node = {
    E.div(
      Component(HeaderComponent),
      get(page)
        .map(Component(PageComponent, _))
        .getOrElse(E.div(Text("Not found")))
    )
  }
}
