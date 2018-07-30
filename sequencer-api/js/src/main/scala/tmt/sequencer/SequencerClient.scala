package tmt.sequencer

import org.scalajs.dom.raw.EventSource
import tmt.sequencer.api.SequenceResultsWeb

object SequencerClient {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val gateway                    = new WebGateway()
  val feeder: SequenceFeederClient       = new SequenceFeederClient(gateway)
  val editor: SequenceEditorClient       = new SequenceEditorClient(gateway)
  lazy val logger: EventSource           = new EventSource(s"${SequenceResultsWeb.ApiName}/${SequenceResultsWeb.results}")
  lazy val imageEventSource: EventSource = new EventSource(s"${SequenceResultsWeb.ApiName}/images")
}
