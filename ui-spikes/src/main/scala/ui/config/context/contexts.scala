package ui.config.context

import typings.reactLib.reactMod
import typings.reactLib.reactMod.{Provider, ^ ⇒ React}
import ui.config.models.{ConfigState, UiState}

object contexts {
  val ConfigsContext: reactMod.Context[ConfigState] = React.createContext(ConfigState())
  val ConfigProvider: Provider[ConfigState]         = ConfigsContext.Provider

  val UiContext: reactMod.Context[UiState] = React.createContext(UiState())
  val UiProvider: Provider[UiState]        = UiContext.Provider
}
