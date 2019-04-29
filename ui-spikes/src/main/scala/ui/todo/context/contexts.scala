package ui.todo.context

import ui.todo.lib.GenericContext
import ui.todo.models.{Todo, VisibilityFilter}

object TodoListContext         extends GenericContext(Seq.empty[Todo])
object VisibilityFilterContext extends GenericContext[VisibilityFilter](VisibilityFilter.All)
