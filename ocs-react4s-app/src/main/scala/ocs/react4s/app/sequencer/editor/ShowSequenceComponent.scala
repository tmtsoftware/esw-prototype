package ocs.react4s.app.sequencer.editor

import com.github.ahnfelt.react4s._
import ocs.api.client.SequenceEditorJsClient
import play.api.libs.json.Json
import ocs.api.codecs.SequencerJsonSupport
import ocs.react4s.app.theme.{ButtonCss, TextAreaCss}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class ShowSequenceComponent(client: P[SequenceEditorJsClient]) extends Component[NoEmit] with SequencerJsonSupport {

  val sequenceResponse = State("")

  def handleShowSequence(client: SequenceEditorJsClient): Unit = client.status.onComplete {
    case Success(value) => sequenceResponse.set(Json.prettyPrint(Json.toJson(value)))
    case Failure(ex)    => sequenceResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      A.className("card-panel", "hoverable"),
      E.h6(Text("Sequence Editor Show Sequence")),
      E.div(
        E.button(
          ButtonCss,
          Text("Show Sequence"),
          A.onClick { e =>
            e.preventDefault()
            handleShowSequence(get(client))
          }
        ),
        E.div(
          Text("Sequence Editor - Show Sequence Response"),
          E.div(
            TextAreaCss,
            E.span(E.pre(Text(get(sequenceResponse))))
          )
        )
      )
    )
  }
}
