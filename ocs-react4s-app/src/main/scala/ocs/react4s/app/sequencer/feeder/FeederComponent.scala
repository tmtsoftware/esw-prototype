package ocs.react4s.app.sequencer.feeder

import com.github.ahnfelt.react4s._
import ocs.api.client.SequencerCommandServiceJsClient
import ocs.api.codecs.SequencerJsonSupport
import ocs.api.models.Sequence
import ocs.react4s.app.IOOperationComponent
import ocs.react4s.app.IOOperationComponent.HandleClick
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

case class FeederComponent(client: P[SequencerCommandServiceJsClient]) extends Component[NoEmit] with SequencerJsonSupport {

  val feedResponse = State("")

  def handleFeed(client: SequencerCommandServiceJsClient, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      feedResponse.set("Waiting for Response ....")
      Try(Json.parse(request).as[Sequence])
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
