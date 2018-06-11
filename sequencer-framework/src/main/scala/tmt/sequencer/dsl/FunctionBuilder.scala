package tmt.sequencer.dsl

import scala.collection.mutable

class FunctionBuilder[I, O] {
  private val handlers: mutable.Buffer[PartialFunction[I, O]] = mutable.Buffer.empty

  lazy val combinedHandler: PartialFunction[I, O] = handlers.foldLeft(PartialFunction.empty[I, O])(_ orElse _)

  def addHandler(p: I => Boolean)(handler: I => O): Unit = handlers += {
    case input if p(input) => handler(input)
  }

  def build(default: I => O): I => O = input => combinedHandler.lift(input).getOrElse(default(input))
}
