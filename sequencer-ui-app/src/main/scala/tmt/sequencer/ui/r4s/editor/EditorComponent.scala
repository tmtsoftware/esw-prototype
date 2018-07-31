package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport

case class EditorComponent(editorClient: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {

  override def render(get: Get): ElementOrComponent = {
    val client = get(editorClient)
    E.div(
      Component(ShowSequenceComponent, client),
      Component(PauseComponent, client),
      Component(ResumeComponent, client),
      Component(ResetComponent, client)
    )
  }
}
