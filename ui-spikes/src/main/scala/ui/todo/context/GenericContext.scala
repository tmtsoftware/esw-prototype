package ui.todo.context

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{Context, FC, ProviderProps, ^ => React}

import scala.scalajs.js

class GenericContext[V, C](default: V, f: (V, V => Unit) => C) {
  val Context: Context[C] = React.createContext(f(default, _ => ()))

  val Provider: FC[_] = define.fc[js.Any] { props =>
    val js.Tuple2(value, set) = React.useState[V](default)

    Context.Provider_Original
      .asInstanceOf[FC[ProviderProps[C]]]
      .props(
        ProviderProps(
          f(value, x => set(x)),
          props.children.getOrElse(null)
        )
      )
  }
}
