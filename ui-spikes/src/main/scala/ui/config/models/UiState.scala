package ui.config.models

import ui.todo.lib.GenericState

case class UiState(addModalOpen: Boolean, setModalOpen: Boolean ⇒ Unit, error: Boolean, setError: Boolean ⇒ Unit) {
  def openAddModal(): Unit  = setModalOpen(true)
  def closeAddModal(): Unit = setModalOpen(false)

  def removeError(): Unit = setError(false)
}

object UiState {
  def apply(): UiState = {
    val (addModalOpen, setModalOpen) = GenericState.use(false)
    val (error, setError)            = GenericState.use(false)
    new UiState(addModalOpen, setModalOpen, error, setError)
  }
}
