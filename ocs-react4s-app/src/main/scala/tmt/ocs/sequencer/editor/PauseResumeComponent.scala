package tmt.ocs.sequencer.editor

import com.github.ahnfelt.react4s._
import tmt.ocs.client.SequenceEditorJsClient
import tmt.ocs.codecs.SequencerJsonSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class PauseResumeComponent(client: P[SequenceEditorJsClient]) extends Component[NoEmit] with SequencerJsonSupport {

  val pauseResponse = State("")
  val buttonText    = State("pause")

  def handleAction(client: SequenceEditorJsClient, action: String): Unit = {
    val (api, text) = action match {
      case "pause" =>
        (client.pause(), "play_arrow")
      case _ =>
        (client.resume(), "pause")
    }
    api.onComplete {
      case Success(_) =>
        buttonText.set(text)
        pauseResponse.set("Operation Successful")
      case Failure(ex) => pauseResponse.set(ex.getMessage)
    }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      A.className("card-panel", "hoverable"),
      E.h6(Text("Sequence Editor Pause/Resume")),
      E.div(
        E.a(
          A.className("btn-large"),
          E.i(
            A.className("material-icons"),
            Text(get(buttonText)),
            A.onClick { e =>
              e.preventDefault()
              handleAction(get(client), get(buttonText))
            }
          )
        ),
        E.span(Text(get(pauseResponse)))
      )
    )
  }
}
