package tmt.sequencer
import com.github.ahnfelt.react4s._
import tmt.WebClients
import tmt.sequencer.models.SequencerInfo
import tmt.sequencer.r4s.editor.EditorComponent
import tmt.sequencer.r4s.feeder.FeederComponent
import tmt.sequencer.r4s.resultevent.ResultEventComponent

case class SequencerComponent(sequencerInfo: P[SequencerInfo]) extends Component[NoEmit] {

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(FeederComponent, WebClients.feeder(get(sequencerInfo))),
      Component(EditorComponent, WebClients.editor(get(sequencerInfo))),
      Component(ResultEventComponent, WebClients.logger(get(sequencerInfo)))
    )
  }
}
