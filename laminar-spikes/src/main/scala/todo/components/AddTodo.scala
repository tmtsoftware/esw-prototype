package todo.components

import com.raquo.laminar.api.L._
import todo.context.TodoListContext

class AddTodo private (
    val node: Div
)

object AddTodo {
  def apply(): AddTodo = {
    val text1           = Var("")
    val sink: Var[Unit] = Var(())

    val node: Div = div(
      form(
        onSubmit.preventDefault.map(_ => TodoListContext.add(text1.now())) --> sink.writer,
        input(
          inContext(thisNode => onInput.mapTo(thisNode.ref.value) --> text1.writer)
        ),
        button(
          tpe := "submit",
          value := "Add Todo"
        )
      )
    )

    new AddTodo(node)
  }
}
