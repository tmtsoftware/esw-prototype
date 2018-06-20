package tmt.sequencer.scripts

import tmt.sequencer.dsl.{CswServices, Script}

class ScriptLoader(scriptConfigs: ScriptConfigs, cswServices: CswServices) {

  def load(): Script = {
    getInstance(loadClass(scriptConfigs.scriptClass))
  }

  private[tmt] def loadClass(scriptClass: String): Class[_] = {
    getClass.getClassLoader
      .loadClass(scriptClass)
  }

  private[tmt] def getInstance(clazz: Class[_]): Script = {
    clazz.getConstructor(classOf[CswServices]).newInstance(cswServices).asInstanceOf[Script]
  }

}
