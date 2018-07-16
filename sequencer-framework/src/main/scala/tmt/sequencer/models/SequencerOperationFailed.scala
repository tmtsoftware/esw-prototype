package tmt.sequencer.models

case class SequencerOperationFailed(msg: String) extends RuntimeException(msg) {
  def apply(msg: String): SequencerOperationFailed = SequencerOperationFailed(s"SequencerOperation failed due to $msg")
}
