package ui.todo.lib

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC

import scala.scalajs.js

object ComponentUtils {
  def compose(comps: FC[_]*): FC[_] = comps.foldLeft(Empty) { (acc, elm) =>
    define.fc[js.Any] { props =>
      acc.noprops(elm.noprops(props.children.getOrElse(null)))
    }
  }

  val Empty: FC[js.Any] = define.fc[js.Any] { props =>
    div.noprops(props.children.getOrElse(null))
  }
}
