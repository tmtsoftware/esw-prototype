package tmt.sequencer.ui.r4s.editor

import com.github.ahnfelt.react4s._
import tmt.sequencer.SequenceEditorClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.SequencerConstants
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss, TextAreaCss}

import scala.util.{Failure, Success}

case class ShowSequenceComponent(client: P[SequenceEditorClient]) extends Component[NoEmit] with WebRWSupport {
  val sequenceResponse = State("")

  import scala.concurrent.ExecutionContext.Implicits.global

  def handleShowSequence(client: SequenceEditorClient): Unit =
    client.sequenceWeb.onComplete {
      case Success(value) => sequenceResponse.set(upickle.default.write(value, 2))
      case Failure(ex)    => sequenceResponse.set(ex.getMessage)
    }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      E.div(
        OperationTitleCss,
        E.button(
          ButtonCss,
          Text(SequencerConstants.SHOW_SEQUENCE_OPERATION),
          A.onClick(e => {
            e.preventDefault()
            handleShowSequence(get(client))
          })
        )
      ),
      E.div(
        OperationTitleCss,
        Text(s"${SequencerConstants.SHOW_SEQUENCE_COMPONENT} Response"),
        E.div(
          TextAreaCss,
          E.span(
            E.pre(
              Text(
                get(sequenceResponse)
              )
            )
          )
        )
      )
    )
  }
}
