package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.SequencerClient
import tmt.sequencer.ui.r4s.editor.PauseComponent
import tmt.sequencer.ui.r4s.facade.NpmReactBridge

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Main {
  def main(arguments: Array[String]): Unit = {
    val component = E.div(
      Text("Interoperability Demo - Reuse js component in scala.js"),
      Component(PauseComponent, SequencerClient.editor),
      Button(J("color", "secondary"), S.left.px(227), S.position.fixed(), Text("Click"))
    )
    NpmReactBridge.renderToDomById(component, "main")
  }
}
@js.native
@JSImport("@material-ui/core", JSImport.Namespace)
object Material extends js.Object {
  val Button: js.Dynamic = js.native
}

object Button extends JsComponent(Material.Button)
