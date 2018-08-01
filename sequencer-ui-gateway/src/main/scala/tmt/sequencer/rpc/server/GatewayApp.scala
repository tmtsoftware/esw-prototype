package tmt.sequencer.rpc.server

object GatewayApp {
  def main(args: Array[String]): Unit = {
    val port = args.headOption.map(_.toInt)

    val wiring = new Wiring(port)

    wiring.rpcServer.start()
  }
}
