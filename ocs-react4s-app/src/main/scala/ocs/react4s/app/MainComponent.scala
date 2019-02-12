package ocs.react4s.app

import com.github.ahnfelt.react4s._
import org.scalajs.dom

case class MainComponent() extends Component[NoEmit] {
  private def path: String = {
    val hashPath = dom.window.location.hash.drop(1)
    if (hashPath.nonEmpty) hashPath else Routes.home
  }

  val page = State(Option(path))

  dom.window.onhashchange = { _ =>
    page.set(Option(s"$path"))
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
