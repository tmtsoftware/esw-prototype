package ocs.framework.dsl.epic

import ocs.framework.CswSystem

object Demo {

  def main(args: Array[String]): Unit = {
    val cswSystem       = new CswSystem("demo")
    val externalService = new ExternalService(cswSystem)
    new TempSS(cswSystem, externalService)
  }

}
