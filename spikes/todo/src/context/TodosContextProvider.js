import React, {useState, useContext} from 'react'
import TodosContext from './TodosContext'
import NotificationContext from './NotificationContext'

const TodosContextProvider = (props) => {
    const [todos, setTodos] = useState([])
    const context = useContext(NotificationContext)


    const addTodo = (text) => {
        setTodos([
            ...todos,
            {
                id: todos.length,
                text: text,
                completed: false
            }])
        context.showNotification("Todo added")
    };

    const toggleTodo = (id) => {
        setTodos(todos.map(todo =>
            todo.id === id ? {...todo, completed: !todo.completed} : todo
        ))
    };

    return (
        <TodosContext.Provider value={
            {
                todos,
                addTodo,
                toggleTodo,
            }
        }>
            {props.children}
        </TodosContext.Provider>
    )

}

export default TodosContextProvider


