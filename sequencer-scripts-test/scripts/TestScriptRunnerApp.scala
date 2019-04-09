import ocs.framework.ScriptRunnerWiring

object TestScriptRunnerApp {
  def main(args: Array[String]): Unit = {
    val scriptLoaderName = args match {
      case Array(name) => name
      case _           => throw new RuntimeException("please provide name for for ScriptLoader")
    }
    new ScriptRunnerWiring(scriptLoaderName).start()
  }
}
