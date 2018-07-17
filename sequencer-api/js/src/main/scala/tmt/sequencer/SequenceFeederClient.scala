package tmt.sequencer

import org.scalajs.dom.ext.Ajax
import tmt.sequencer.api.SequenceFeederWeb
import tmt.sequencer.models.{AggregateResponseWeb, CommandListWeb, WebRWSupport}

import scala.concurrent.{ExecutionContext, Future}

class SequenceFeederClient()(implicit ec: ExecutionContext) extends SequenceFeederWeb with WebRWSupport {

  override def feed(commandList: CommandListWeb): Future[AggregateResponseWeb] = {
    val url = s"${SequenceFeederWeb.ApiName}/${SequenceFeederWeb.Feed}"
    Ajax
      .post(
        url = url,
        data = upickle.default.write(commandList),
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
