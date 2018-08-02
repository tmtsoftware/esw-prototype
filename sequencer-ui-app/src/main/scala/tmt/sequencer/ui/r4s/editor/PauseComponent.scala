package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.SequenceEditorWebClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class PauseComponent(client: P[SequenceEditorWebClient]) extends Component[NoEmit] with WebRWSupport {

  val pauseResponse = State("")

  def handlePause(client: SequenceEditorWebClient): Unit = client.pause().onComplete {
    case Success(_)  => pauseResponse.set("Operation Successful")
    case Failure(ex) => pauseResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      E.button(
        ButtonCss,
        Text("Pause Sequence"),
        A.onClick { e =>
          e.preventDefault()
          handlePause(get(client))
        }
      ),
      Text(get(pauseResponse))
    )
  }
}
