package tmt.sequencer.rpc.server

object SequencerComponent {
  def getComponentName(sequencerId: String, observingMode: String) = s"$sequencerId@$observingMode"
}
