package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.client.ListSequencersClient
import tmt.sequencer.models.WebRWSupport
import tmt.sequencer.ui.r4s.theme.{ButtonCss, OperationTitleCss, ResultTextAreaCss}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class ListSequencersComponent(client: P[ListSequencersClient]) extends Component[NoEmit] with WebRWSupport {

  val ListSequencerResponse: State[List[String]] = State(List.empty[String])

  def handleListSequencers(client: ListSequencersClient): Unit = client.listSequencers.onComplete {
    case Success(result) => ListSequencerResponse.set(result)
    case Failure(ex)     => ListSequencerResponse.set(List(ex.getMessage))
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      OperationTitleCss,
      E.button(
        ButtonCss,
        Text("List Sequencers"),
        A.onClick { e =>
          e.preventDefault()
          handleListSequencers(get(client))
        }
      ),
      E.ul(
        ResultTextAreaCss,
        Tags(
          get(ListSequencerResponse).map(
            sequencer =>
              E.li(
                E.a(
                  A.href(sequencer),
                  Text(sequencer)
                )
            )
          )
        )
      )
    )
  }
}
