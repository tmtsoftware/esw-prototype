package ui.todo.context

import ui.todo.models.VisibilityFilter

object VisibilityFilterContext extends GenericContext(VisibilityFilter.All, new VisibilityFilterContext(_, _))

case class VisibilityFilterContext(filter: VisibilityFilter, set: VisibilityFilter => Unit)
