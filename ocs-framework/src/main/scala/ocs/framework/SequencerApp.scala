package ocs.framework

import io.lettuce.core.RedisClient

object SequencerApp extends SequencerAppTemplate

class SequencerAppTemplate {
  def main(args: Array[String]): Unit = {
    args match {
      case Array("-seqcomp", name) =>
        new SequenceComponentWiring(name).start()
      case Array("-sequencer", sequencerId, observingMode) =>
        lazy val redisClient: RedisClient = RedisClient.create()
        lazy val cswSystem                = new CswSystem("csw-system")
        new SequencerWiring(sequencerId, observingMode, cswSystem, redisClient).start()
      case _ => println("""
          |please provide one of these alternatives:
          |-seqcomp sequencerComponentName
          |-sequencer sequencerId observingMode
          |""".stripMargin)
    }
  }
}
