package tmt.sequencer.r4s

import com.github.ahnfelt.react4s._

case class HeaderComponent() extends Component[NoEmit] {
  override def render(get: Get): Node = {
    E.nav(
      A.className("teal"),
      E.div(
        A.className("nav-wrapper"),
        E.a(
          A.href("#"),
          A.className("brand-logo"),
          Text("TMT")
        ),
        E.ul(
          A.className("right hide-on-med-and-down"),
          E.li(
            E.a(
              A.href("https://github.com/tmtsoftware/esw-prototype"),
              Text("Github")
            )
          )
        )
      )
    )
  }
}
