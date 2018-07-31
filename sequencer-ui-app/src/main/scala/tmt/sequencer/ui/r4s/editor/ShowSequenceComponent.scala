package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss, TextAreaCss}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class ShowSequenceComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {

  val sequenceResponse = State("")

  def handleShowSequence(client: SequenceEditorClient): Unit = client.sequenceWeb.onComplete {
    case Success(value) => sequenceResponse.set(upickle.default.write(value, indent = 2))
    case Failure(ex)    => sequenceResponse.set(ex.getMessage)
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      E.div(
        OperationTitleCss,
        E.button(
          ButtonCss,
          Text("Show Sequence"),
          A.onClick { e =>
            e.preventDefault()
            handleShowSequence(get(client))
          }
        )
      ),
      E.div(
        OperationTitleCss,
        Text("Sequence Editor - Show Sequence Response"),
        E.div(
          TextAreaCss,
          E.span(E.pre(Text(get(sequenceResponse))))
        )
      )
    )
  }
}
