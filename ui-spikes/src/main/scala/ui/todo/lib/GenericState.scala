package ui.todo.lib

import typings.reactLib.reactMod.{^ => React}

import scala.scalajs.js

object GenericState {
  def use[T](init: T): (T, T => Unit) = {
    val js.Tuple2(value, set) = React.useState(init)
    (value, x => set(x))
  }
}
