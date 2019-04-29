package ui.todo.lib

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{Context, FC, ProviderProps, ^ => React}

import scala.scalajs.js

class GenericContext[T](default: T) {

  def use(): ContextType[T] = React.useContext(Context)

  private val Context: Context[ContextType[T]] = React.createContext(ContextType(default, _ => ()))

  val Provider: FC[_] = define.fc[js.Any] { props =>
    val (value, set) = GenericState.use(default)

    Context.Provider_Original
      .asInstanceOf[FC[ProviderProps[ContextType[T]]]]
      .props(
        ProviderProps(
          ContextType(value, x => set(x)),
          props.children.getOrElse(null)
        )
      )
  }
}
