package tmt.ocs.sequencer.editor

import com.github.ahnfelt.react4s._
import tmt.ocs.client.SequenceEditorJsClient
import tmt.ocs.codecs.SequencerJsonSupport

case class EditorComponent(editorClient: P[SequenceEditorJsClient]) extends Component[NoEmit] with SequencerJsonSupport {

  override def render(get: Get): ElementOrComponent = {
    val client = get(editorClient)
    E.div(
      Component(ShowSequenceComponent, client),
      Component(PauseResumeComponent, client),
      Component(ResetComponent, client)
    )
  }
}
