package tmt
import tmt.ocs.models.InstantNow

object TestInstantJVM {
  def main(args: Array[String]): Unit = {
    println(InstantNow.time)
  }
}
