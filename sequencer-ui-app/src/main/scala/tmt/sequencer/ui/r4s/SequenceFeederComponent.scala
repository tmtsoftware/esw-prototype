package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.SequenceFeederClient
import tmt.sequencer.models.WebRWSupport

case class SequenceFeederComponent(client: P[SequenceFeederClient]) extends Component[NoEmit] with WebRWSupport {

  override def render(get: Get): ElementOrComponent = {
    E.div(Component(FeederIOComponent, "Sequence Feeder", "Submit Sequence", get(client)))
  }
}
