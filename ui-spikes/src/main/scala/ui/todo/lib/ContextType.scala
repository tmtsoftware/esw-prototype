package ui.todo.lib

case class ContextType[T](get: T, set: T => Unit)
