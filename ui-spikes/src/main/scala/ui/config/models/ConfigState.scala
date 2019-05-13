package ui.config.models

import ui.todo.lib.GenericState

case class ConfigState(items: Seq[Item], setItems: Seq[Item] ⇒ Unit) {

  def addItem(item: Item): Unit = setItems(items :+ item)

  def removeItem(path: String): Unit = setItems(items.filter(item => item.path != path))

}

object ConfigState {
  def apply(): ConfigState = {
    val (items, setItems) = GenericState.use(Seq.empty[Item])
    new ConfigState(items, setItems)
  }
}
