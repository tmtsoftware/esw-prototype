package ocs.framework.util

import ocs.framework.Configs
import ocs.framework.dsl.{CswServices, Script}

//merge with ScriptConfigs
object ScriptLoader {
  //should load take params and remove a few dependencies
  def load(configs: Configs, cswServices: CswServices): Script = {
    val clazz = getClass.getClassLoader.loadClass(configs.scriptClass)
    clazz.getConstructor(classOf[CswServices]).newInstance(cswServices).asInstanceOf[Script]
  }
}
