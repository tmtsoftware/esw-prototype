package tmt.sequencer.ui.r4s.interoperability

import com.github.ahnfelt.react4s.{Component, ReactBridge}
import tmt.sequencer.ui.r4s.HeaderComponent
import tmt.sequencer.ui.r4s.facade.NpmReactBridge

import scala.scalajs.js.annotation.{JSExportAll, JSExportTopLevel}

@JSExportTopLevel("Interoperability")
@JSExportAll
object Interoperability {
  val Header: ReactBridge.ReactElement = NpmReactBridge.componentToReact(Component(HeaderComponent))
}
