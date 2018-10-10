package ocs.react4s.app.assembly

import com.github.ahnfelt.react4s._
import csw.params.commands.ControlCommand
import ocs.api.client.AssemblyFeederJsClient
import play.api.libs.json.Json
import ocs.api.codecs.SequencerJsonSupport
import ocs.react4s.app.IOOperationComponent
import ocs.react4s.app.IOOperationComponent.HandleClick

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

case class AssemblyCommandComponent(client: P[AssemblyFeederJsClient]) extends Component[NoEmit] with SequencerJsonSupport {

  val submitResponse = State("")

  def handleSubmit(client: AssemblyFeederJsClient, msg: IOOperationComponent.Msg): Unit = msg match {
    case HandleClick(request) =>
      submitResponse.set("Waiting for Response ....")
      Try(Json.parse(request).as[ControlCommand])
        .map(
          input =>
            client.submit(input).onComplete {
              case Success(response) => submitResponse.set(Json.prettyPrint(Json.toJson(response)))
              case Failure(ex)       => submitResponse.set(ex.getMessage)
          }
        )
        .recover { case ex => submitResponse.set("Invalid input request, please verify the input provided") }
  }

  override def render(get: Get): ElementOrComponent = {
    E.div(
      Component(
        IOOperationComponent,
        "Assembly Commands - Submit",
        "Submit Commands",
        get(submitResponse)
      ).withHandler(x => handleSubmit(get(client), x))
    )
  }
}
