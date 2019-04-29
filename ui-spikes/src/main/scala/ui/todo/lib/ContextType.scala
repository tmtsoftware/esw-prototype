package ui.todo.lib

case class ContextType[T](value: T, set: T => Unit)
