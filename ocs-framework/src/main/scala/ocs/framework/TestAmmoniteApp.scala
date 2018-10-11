package ocs.framework

import csw.logging.scaladsl.LoggingSystemFactory

object TestAmmoniteApp {

  def main(args: Array[String]): Unit = {
    val wiring = new TestAmmoniteWiring()
    import wiring._
    LoggingSystemFactory.start("sample", "", "", system)

    ammonite
      .Main(
        predefCode = "println(\"Start with location service, command service, sequencer APIs!\")"
      )
      .run(
        "locationService" -> locationServiceWrapper,
        "commandService"  -> commandServiceWrapper,
        "sequencerApi"    -> sequencerApiWrapper
      )
  }
}
