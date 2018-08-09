package tmt

import com.github.ahnfelt.react4s._
import tmt.sequencer.editor.EditorComponent
import tmt.sequencer.feeder.FeederComponent
import tmt.sequencer.resultevent.ResultEventComponent

case class SequencerComponent() extends Component[NoEmit] {
  override def render(get: Get): Node = {
    E.div(
      Component(HeaderComponent),
      Component(FeederComponent, WebClients.feeder),
      Component(EditorComponent, WebClients.editor),
      Component(ResultEventComponent, WebClients.logger)
    )
  }
}
