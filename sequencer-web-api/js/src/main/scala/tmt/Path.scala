package tmt

import org.scalajs.dom

object Path {
  def hashPath: String = dom.window.location.hash.drop(1)
}
