package runner
import java.lang.management.ManagementFactory

import tmt.ocs.SequencerApp

object TestSequencerApp {
  def main(args: Array[String]): Unit = {
    val runtimeMXBean = ManagementFactory.getRuntimeMXBean
    val begin         = runtimeMXBean.getStartTime
    val jvmStarted    = System.currentTimeMillis

    import scala.concurrent.ExecutionContext.Implicits.global
    val (sequencerId, observingMode, replPort) = args match {
      case Array(sId, oMode, p) => (sId, oMode, p.toInt)
      case _                    => throw new RuntimeException("please provide sequencerId, observationMode and ssh repl port parameters")
    }
    SequencerApp.run(sequencerId, observingMode, replPort).map { _ =>
      val appStarted = System.currentTimeMillis

      println(s"---------------------------jvm: ${jvmStarted - begin}")
      println(s"---------------------------app: ${appStarted - jvmStarted}")
      println(s"---------------------------total: ${appStarted - begin}")
    }
  }
}
