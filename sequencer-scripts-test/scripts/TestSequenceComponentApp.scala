import ocs.framework.SequenceComponentWiring

object TestSequenceComponentApp {
  def main(args: Array[String]): Unit = {
    val sequenceComponentName = args match {
      case Array(name) => name
      case _           => throw new RuntimeException("please provide name for for Sequence Component")
    }
    new SequenceComponentWiring(sequenceComponentName).start()
  }
}
