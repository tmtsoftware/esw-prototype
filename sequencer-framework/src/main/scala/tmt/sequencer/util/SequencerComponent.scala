package tmt.sequencer.util

object SequencerComponent {
  def getComponentName(sequencerId: String, observingMode: String) = s"$sequencerId@$observingMode"
}
