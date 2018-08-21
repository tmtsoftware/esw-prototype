package tmt.sequencer.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.SequenceEditorWebClient
import tmt.sequencer.codecs.SequencerWebRWSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class PauseComponent(client: P[SequenceEditorWebClient]) extends Component[NoEmit] with SequencerWebRWSupport {

  val pauseResponse = State("")

  def handlePause(client: SequenceEditorWebClient): Unit = client.pause().onComplete {
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
