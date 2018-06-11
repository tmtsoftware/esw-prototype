package tmt.sequencer

object SequencerApp {
  def run(sequencerId: String, observingMode: String, port: Option[Int]): Unit = {
    val wiring = new Wiring(sequencerId, observingMode, port)
    import wiring._
    engine.start(sequencer, script)
    rpcServer2.start()
    supervisorRef
    remoteRepl.server().start()
  }
}
