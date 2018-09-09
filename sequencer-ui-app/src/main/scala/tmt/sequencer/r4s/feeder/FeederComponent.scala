package tmt.sequencer.r4s.feeder

import com.github.ahnfelt.react4s._
import play.api.libs.json.Json
import tmt.sequencer.client.SequenceFeederClient
import tmt.sequencer.codecs.SequencerJsonSupport
import tmt.sequencer.models.CommandList
import tmt.sequencer.r4s.IOOperationComponent
import tmt.sequencer.r4s.IOOperationComponent.HandleClick

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

case class FeederComponent(client: P[SequenceFeederClient]) extends Component[NoEmit] with SequencerJsonSupport {

  val feedResponse = State("")

  def handleFeed(client: SequenceFeederClient, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      feedResponse.set("Waiting for Response ....")
      Try(Json.parse(request).as[CommandList])
        .map(
          input =>
            client.feed(input).onComplete {
              case Success(response) => feedResponse.set("Operation Successful")
              case Failure(ex)       => feedResponse.set(ex.getMessage)
          }
        )
        .recover { case ex => feedResponse.set("Invalid input request, please verify the input provided") }

  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(
        IOOperationComponent,
        "Sequence Feeder - Feed",
        "Feed Sequence",
        get(feedResponse)
      ).withHandler(x => handleFeed(get(client), x))
    )
  }
}
