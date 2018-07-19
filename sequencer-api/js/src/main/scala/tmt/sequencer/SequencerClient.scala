package tmt.sequencer

import org.scalajs.dom.raw.EventSource
import tmt.sequencer.api.SequenceLoggerWeb

//TODO: discuss
object SequencerClient {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val gateway              = new WebGateway()
  val feeder: SequenceFeederClient = new SequenceFeederClient(gateway)
  val editor: SequenceEditorClient = new SequenceEditorClient(gateway)
  val logger: EventSource          = new EventSource(s"${SequenceLoggerWeb.ApiName}/${SequenceLoggerWeb.results}")
}
