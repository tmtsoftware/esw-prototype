package ocs.react4s.app

import com.github.ahnfelt.react4s._
import ocs.api.WebClients
import ocs.api.models.SequencerInfo
import ocs.react4s.app.sequencer.editor.EditorComponent
import ocs.react4s.app.sequencer.feeder.FeederComponent
import ocs.react4s.app.sequencer.resultevent.ResultEventComponent

case class SequencerComponent(sequencerInfo: P[SequencerInfo]) extends Component[NoEmit] {

  override def render(get: Get): ElementOrComponent = {
    E.div(
      A.className("row"),
      E.div(
        A.className("col s6"),
        Component(FeederComponent, WebClients.feeder(get(sequencerInfo))),
        Component(EditorComponent, WebClients.editor(get(sequencerInfo)))
      ),
      E.div(
        A.className("col s6"),
        Component(ResultEventComponent, WebClients.results(get(sequencerInfo)))
      )
    )
  }
}
