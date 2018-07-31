package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class ResetComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {

  val resetResponse = State("")

  def handleReset(client: SequenceEditorClient): Unit = client.reset().onComplete {
    case Success(_)  => resetResponse.set("Operation Successful")
    case Failure(ex) => resetResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      E.button(
        ButtonCss,
        Text("Reset Sequence"),
        A.onClick { e =>
          e.preventDefault()
          handleReset(get(client))
        }
      ),
      Text(get(resetResponse))
    )
  }
}
