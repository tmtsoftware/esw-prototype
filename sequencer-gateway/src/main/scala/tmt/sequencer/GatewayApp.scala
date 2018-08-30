package tmt.sequencer

object GatewayApp extends App {
    val port = args.headOption.map(_.toInt)

    val wiring = new Wiring(port)

    wiring.server.start()
}
