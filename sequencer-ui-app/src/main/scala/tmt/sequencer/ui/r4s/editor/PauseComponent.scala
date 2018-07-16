package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.SequencerConstants
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss}

import scala.util.{Failure, Success}

case class PauseComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {
  val pauseResponse = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  def handlePause(client: SequenceEditorClient): Unit = client.pause().onComplete {
    case Success(_) => pauseResponse.set(SequencerConstants.SUCCESS_MSG)
    case Failure(ex) =>
      println(ex)
      pauseResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      E.button(
        ButtonCss,
        Text(SequencerConstants.PAUSE_OPERATION),
        A.onClick(e => {
          e.preventDefault()
          handlePause(get(client))
        })
      ),
      Text(get(pauseResponse))
    )
  }
}
