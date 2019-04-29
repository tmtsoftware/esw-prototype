import React, {useState} from 'react'
import TodosContext from '../context/TodosContext'

const TodosContextProvider = (props) => {
    const [todos, setTodos] = useState([])


    const addTodo = (text) => {
        setTodos([
            ...todos,
            {
                id: todos.length,
                text: text,
                completed: false
            }])
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


