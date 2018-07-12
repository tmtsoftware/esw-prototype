package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s.{E, _}
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss}

import scala.util.{Failure, Success}

case class ResetComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {
  val ResetResponse = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  def handleReset(get: Get): Unit = get(client).reset().onComplete {
    case Success(_)  => ResetResponse.set("Done")
    case Failure(ex) => ResetResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      E.button(ButtonCss, Text("Reset Sequence"), A.onClick(e => {
        e.preventDefault()
        handleReset(get)
      }))
    )
  }
}
