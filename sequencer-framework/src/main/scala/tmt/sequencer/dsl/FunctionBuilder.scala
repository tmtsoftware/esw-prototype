package tmt.sequencer.dsl

import scala.collection.mutable
import scala.reflect.ClassTag

class FunctionBuilder[I, O] {
  private val handlers: mutable.Buffer[PartialFunction[I, O]] = mutable.Buffer.empty

  lazy val combinedHandler: PartialFunction[I, O] = handlers.foldLeft(PartialFunction.empty[I, O])(_ orElse _)

  def addHandler[T <: I](handler: T => O)(iff: T => Boolean): Unit = handlers += {
    case input: T if iff(input) => handler(input)
  }

  def build(default: I => O): I => O = input => combinedHandler.lift(input).getOrElse(default(input))
}
