import io.lettuce.core.RedisClient
import ocs.framework.{CswSystem, Wiring}

object TestSequencerApp {
  def main(args: Array[String]): Unit = {
    val (sequencerId, observingMode) = args match {
      case Array(sId, oMode) => (sId, oMode)
      case _                 => throw new RuntimeException("please provide sequencerId, ")
    }
    lazy val redisClient: RedisClient = RedisClient.create()
    lazy val cswSystem                = new CswSystem("csw-system")

    new Wiring(sequencerId, observingMode, cswSystem, redisClient).start()
  }
}
