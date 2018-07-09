package tmt.sequencer

import org.scalajs.dom.ext.Ajax
import tmt.sequencer.api.SequenceFeederWeb
import tmt.sequencer.models.{AggregateResponseWeb, CommandListWeb}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

class SequenceFeederClient(baseUri: String)(implicit ec: ExecutionContext) extends SequenceFeederWeb {

  override def feed(commandList: CommandListWeb): Future[AggregateResponseWeb] = {
    val url = s"$baseUri/${SequenceFeederWeb.ApiName}/${SequenceFeederWeb.Feed}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write[CommandListWeb](commandList),
        headers = Map(
          "Content-Type" -> "application/json"
        )
      )
      .map { xhr =>
        println(xhr.responseText)
        upickle.default.read[AggregateResponseWeb](xhr.responseText)
      }
  }
}
