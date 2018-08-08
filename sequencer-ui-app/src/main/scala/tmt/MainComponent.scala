package tmt
import com.github.ahnfelt.react4s._
import org.scalajs.dom

case class MainComponent() extends Component[NoEmit] {
  def href(page: Page): String =
    if (dom.window.location.href.contains("?"))
      "#" + Routes.router.path(page)
    else
      Routes.router.path(page)

  def path() =
    if (dom.window.location.href.contains("?"))
      dom.window.location.hash.drop(1)
    else
      dom.window.location.pathname

  val page = State(Routes.router.data(path()))

//  dom.window.onhashchange = { _ =>
//    println("onhashchange")
//    page.set(Routes.router.data(path()))
//  }

  dom.window.onbeforeunload = { _ =>
    println("onbeforeunload")
    println("*********************")
    println(path())
    page.set(Routes.router.data(path()))
  }

//  dom.window.onchange = { _ =>
//    println("onchange")
//    page.set(Routes.router.data(path()))
//  }

  override def render(get: Get): Node = {
    println(get(page))
    E.div(
      E.a(Text("Home Page"), A.href(href(Home))),
      get(page).map(Component(PageComponent, _)).getOrElse(E.div(Text("Not found")))
    )
  }
}
