package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._

case class MainComponent() extends Component[NoEmit] {
  override def render(get: Get): Node = {
    E.div(
      Component(HeaderComponent),
      Component(OperationFrameComponent)
    )
  }
}