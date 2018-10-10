package ocs.gateway

object GatewayApp extends App {
  val port = args.headOption.map(_.toInt)

  val wiring = new Wiring(port)

  wiring.server.start()
}
