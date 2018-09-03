package tmt.trial

object Main {
  def main(args: Array[String]): Unit = {
    println(KT.IntKT.instance())
    println(KT.IntKT.instance().toPb(10))
  }
}
