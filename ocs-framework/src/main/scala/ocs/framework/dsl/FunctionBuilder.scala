package ocs.framework.dsl

import scala.collection.mutable
import scala.reflect.ClassTag

class FunctionBuilder[I, O] {
  private[dsl] val handlers: mutable.Buffer[PartialFunction[I, O]] = mutable.Buffer.empty

  lazy val combinedHandler: PartialFunction[I, O] = handlers.foldLeft(PartialFunction.empty[I, O])(_ orElse _)

  def addHandler[T <: I: ClassTag](handler: T => O)(iff: T => Boolean): Unit = handlers += {
    case input: T if iff(input) => handler(input)
  }

  def build(default: I => O): I => O = input => combinedHandler.lift(input).getOrElse(default(input))
}

class Function0Handlers[O] {
  private val handlers: mutable.Buffer[() ⇒ O] = mutable.Buffer.empty

  def add(handler: ⇒ O): Unit = handlers += handler _

  def execute(): mutable.Buffer[O] = handlers.map(_.apply())
}


class Function1Handlers[I, O] {
  private val handlers: mutable.Buffer[I ⇒ O] = mutable.Buffer.empty

  def add(handler: I ⇒ O): Unit = handlers += handler

  def execute(input: I): mutable.Buffer[O] = handlers.map(_.apply(input))
}
