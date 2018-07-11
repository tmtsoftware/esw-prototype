package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport

case class SequenceEditorComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {

  override def render(get: Get): ElementOrComponent = {
    E.div(Component(EditorIOComponent, "Sequence Editor", "Add to Sequence", get(client)))
  }
}
