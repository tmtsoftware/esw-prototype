package ui.mirror

import org.scalajs.dom.Event

object Boot {

  def start(renderBackend: RenderBackend): Event => Unit = { _ =>
    val store = new Store
    new ExternalService(store)
    renderBackend.setup()
    new Display(store, renderBackend).render(renderBackend.center, 16, 13)
    renderBackend.postDraw()
  }

}
