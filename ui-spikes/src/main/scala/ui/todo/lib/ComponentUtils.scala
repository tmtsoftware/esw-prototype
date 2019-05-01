package ui.todo.lib

import typings.reactLib.dsl._
import typings.reactLib.reactMod.FC

object ComponentUtils {
  def compose(comps: Seq[FC[_]]): FC[JsUnit] = comps.foldLeft(Empty) { (acc, elm) =>
    define.fc[JsUnit] { props =>
      acc.noprops(elm.noprops(props.children.getOrElse(null)))
    }
  }

  val Empty: FC[JsUnit] = define.fc[JsUnit] { props =>
    div.noprops(props.children.getOrElse(null))
  }
}
