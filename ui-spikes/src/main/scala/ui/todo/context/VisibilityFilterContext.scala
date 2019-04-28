package ui.todo.context

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{Context, FC, ProviderProps, ^ => React}
import ui.todo.models.VisibilityFilter

import scala.scalajs.js

object VisibilityFilterContext {
  val Context: Context[VisibilityFilterContext] = React.createContext(
    new VisibilityFilterContext(VisibilityFilter.All, _ => ())
  )

  val Provider: FC[Unit] = define.fc[Unit] { props =>
    val js.Tuple2(value, set) = React.useState[VisibilityFilter](VisibilityFilter.All)

    Context.Provider(
      ProviderProps(
        new VisibilityFilterContext(value, visibilityFilter => set(visibilityFilter)),
        props.children.get
      )
    )
  }
}

class VisibilityFilterContext(val filter: VisibilityFilter, val set: VisibilityFilter => Unit) extends js.Object
