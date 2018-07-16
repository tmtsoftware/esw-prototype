package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.SequencerConstants
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss}

import scala.util.{Failure, Success}

case class ResetComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {
  val resetResponse = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  def handleReset(client: SequenceEditorClient): Unit = client.reset().onComplete {
    case Success(value) => resetResponse.set(value)
    case Failure(_)     => resetResponse.set(SequencerConstants.ERROR_MSG)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      E.button(ButtonCss, Text(SequencerConstants.RESET_OPERATION), A.onClick(e => {
        e.preventDefault()
        handleReset(get(client))
      })),
      Text(get(resetResponse))
    )
  }
}
