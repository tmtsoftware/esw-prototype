package ui.todo.context

import ui.todo.lib.{ComponentUtils, GenericContext, JsUnit}
import ui.todo.models
import ui.todo.models.{Todo, VisibilityFilter}
import enumeratum._
import typings.reactLib.reactMod.FC

import scala.collection.immutable

sealed trait Context extends EnumEntry {
  def Provider: FC[JsUnit]
}

object Context extends Enum[Context] {
  object TodoList         extends GenericContext(Seq.empty[Todo]) with Context
  object VisibilityFilter extends GenericContext[VisibilityFilter](models.VisibilityFilter.All) with Context

  override def values: immutable.IndexedSeq[Context] = findValues

  val Provider: FC[JsUnit] = ComponentUtils.compose(Context.values.map(_.Provider))
}
