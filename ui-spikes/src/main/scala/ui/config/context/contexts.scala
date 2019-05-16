package ui.config.context

import enumeratum.{Enum, EnumEntry}
import typings.reactLib.reactMod.FC
import ui.config.models.Item
import ui.todo.lib.{ComponentUtils, GenericContext, JsUnit}

import scala.collection.immutable

object contexts {

  sealed trait Context extends EnumEntry {
    def Provider: FC[JsUnit]
  }

  object Context extends Enum[Context] {
    object ConfigStore extends GenericContext(Seq.empty[Item]) with Context
    object ModalOpenStore   extends GenericContext(false) with Context
    object ErrorStore       extends GenericContext("") with Context

    override def values: immutable.IndexedSeq[Context] = findValues

    val Provider: FC[JsUnit] = ComponentUtils.compose(Context.values.map(_.Provider))
  }

}
