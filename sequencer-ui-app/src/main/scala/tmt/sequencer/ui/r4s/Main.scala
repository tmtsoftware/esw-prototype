package tmt.sequencer.ui.r4s

import com.github.ahnfelt.react4s._
import tmt.sequencer.ui.r4s.facade.NpmReactBridge

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Main {
  def main(arguments: Array[String]): Unit = {
    val component = E.div(
      Button(J("color", "secondary"), Text("Click"))
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
