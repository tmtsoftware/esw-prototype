package tmt.sequencer

import com.github.ahnfelt.react4s._
import org.scalajs.dom

case class MainComponent() extends Component[NoEmit] {
  def path(): String =
    if (dom.window.location.href.contains("localhost"))
      dom.window.location.hash.drop(1)
    else
      dom.window.location.pathname

  val page = State(Routes.router.data(path()))

  dom.window.onhashchange = { _ =>
    println("hashchange")
    page.set(Routes.router.data(path()))
  }

  override def render(get: Get): Node = {
    println(get(page))
    E.div(
      get(page)
        .map(Component(PageComponent, _))
        .getOrElse(E.div(Text("Not found")))
    )
  }
}
