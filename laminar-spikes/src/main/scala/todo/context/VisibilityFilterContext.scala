package todo.context
import com.raquo.airstream.signal.Var
import todo.models.VisibilityFilter

object VisibilityFilterContext {
  val visibilityFilter: Var[VisibilityFilter] = Var(VisibilityFilter.All)
}
