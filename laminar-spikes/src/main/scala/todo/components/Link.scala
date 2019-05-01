//package todo.components
//import com.raquo.laminar.api.L._
//import todo.context.{TodoListContext, VisibilityFilterContext}
//import todo.models.VisibilityFilter
//
//class Link private (
//    val node: Div
//)
//
//object Link {
//  def apply(linkText: VisibilityFilter) = {
//    val sink: Var[Unit] = Var(())
//    val node = button(
//      linkText,
//      onClick.map {
//        onSubmit.preventDefault.map(_ => TodoListContext.add(text1.now())) --> sink.writer,
//       ( _ => VisibilityFilterContext.visibilityFilter.set(linkText)) --> sink.writer
//      }
//    )
//  }
//}
