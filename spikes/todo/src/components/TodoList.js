import React, {useContext} from 'react'
import Todo from './Todo'
import TodosContext from '../context/TodosContext'
import {VisibilityFilters} from "../constants/visibilityFilters";
import VisibilityFilterContext from "../context/VisibilityFilterContext";

const TodoList = () => {
    const todosContext = useContext(TodosContext)
    const visibilityFilterContext = useContext(VisibilityFilterContext)

    return <ul>
        {
            getVisibleTodos(todosContext.todos, visibilityFilterContext.visibilityFilter).map(todo => (
                <Todo key={todo.id} {...todo} onClick={() => todosContext.toggleTodo(todo.id)}/>
            ))
        }
    </ul>
}

const getVisibleTodos = (todos, filter) => {
    console.log(`getVisibleTodos ${filter}`)
    switch (filter) {
        case VisibilityFilters.SHOW_COMPLETED:
            return todos.filter(t => t.completed)
        case VisibilityFilters.SHOW_ACTIVE:
            return todos.filter(t => !t.completed)
        default:
            return todos
    }
}

export default TodoList
