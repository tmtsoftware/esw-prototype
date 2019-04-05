import ocs.framework.ScriptLoaderApp

object TestScriptLoaderApp {
  def main(args: Array[String]): Unit = {
    val scriptLoaderName = args match {
      case Array(name) => name
      case _           => throw new RuntimeException("please provide name for for ScriptLoader")
    }
    ScriptLoaderApp.run(scriptLoaderName)
  }
}
