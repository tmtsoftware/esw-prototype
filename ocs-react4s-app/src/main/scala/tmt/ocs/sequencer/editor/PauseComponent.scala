package tmt.ocs.sequencer.editor

import com.github.ahnfelt.react4s._
import tmt.ocs.client.SequenceEditorJsClient
import tmt.ocs.codecs.SequencerJsonSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class PauseComponent(client: P[SequenceEditorJsClient]) extends Component[NoEmit] with SequencerJsonSupport {

  val pauseResponse = State("")

  def handlePause(client: SequenceEditorJsClient): Unit = client.pause().onComplete {
    case Success(_)  => pauseResponse.set("Operation Successful")
    case Failure(ex) => pauseResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      A.className("card-panel", "hoverable"),
      E.h6(Text("Sequence Editor Pause")),
      E.div(
        E.a(
          A.className("btn-large"),
          E.i(
            A.className("material-icons"),
            Text("pause"),
            A.onClick { e =>
              e.preventDefault()
              handlePause(get(client))
            }
          )
        ),
        E.span(Text(get(pauseResponse)))
      )
    )
  }
}
