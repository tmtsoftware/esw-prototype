package ui.todo.lib

import typings.reactLib.dsl._
import typings.reactLib.reactMod.{Context, FC, ProviderProps, ReactElement, ReactNode, ^ => React}

class GenericContext[T](default: T) {

  private val GetterContext = new SingleContext(default)
  private val SetterContext = new SingleContext[T => Unit](_ => ())

  def use(): (T, T => Unit) = (useGetter(), useSetter())

  def useGetter(): T         = GetterContext.use()
  def useSetter(): T => Unit = SetterContext.use()

  val Provider: FC[JsUnit] = define.fc[JsUnit] { props =>
    val (get, set) = GenericState.use(default)
    GetterContext
      .Provider(
        get,
        SetterContext.Provider(
          set,
          props.children.getOrElse(null)
        )
      )
  }
}

private[lib] class SingleContext[T](default: T) {
  def use(): T = React.useContext(Context)

  private val Context: Context[T]                     = React.createContext(default)
  private val Provider_Original: FC[ProviderProps[T]] = Context.Provider_Original.asInstanceOf[FC[ProviderProps[T]]]

  def Provider(value: T, reactNode: ReactNode): ReactElement[ProviderProps[T]] = {
    val providerProps: ProviderProps[T] = ProviderProps(
      React.useMemo(() => value),
      reactNode
    )
    Provider_Original.props(providerProps)
  }
}
