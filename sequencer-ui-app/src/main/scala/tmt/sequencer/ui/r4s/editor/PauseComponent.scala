package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss}

import scala.util.{Failure, Success}

case class PauseComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {
  val PauseResponse = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  def handlePause(client: SequenceEditorClient): Unit = client.pause().onComplete {
    case Success(_)  => PauseResponse.set("Done")
    case Failure(ex) => PauseResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      E.button(ButtonCss, Text("Pause Sequence"), A.onClick(e => {
        e.preventDefault()
        handlePause(get(client))
      }))
    )
  }
}
