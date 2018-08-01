package tmt.sequencer

object SequencerComponent {
  def getComponentName(sequencerId: String, observingMode: String) = s"$sequencerId@$observingMode"
}
