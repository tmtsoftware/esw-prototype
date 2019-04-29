import React, { useContext, useState } from "react";
import TodosContext from "../context/TodosContext";

const AddTodo = () => {
    const context = useContext(TodosContext)
    const [input, setInput] = useState('')

    const updateInput = (e) => {
        setInput( e.target.value)
    }

    return (
        <form
            onSubmit={
                (e) => {
                    e.preventDefault()
                    context.addTodo(input)
                }
            }>
            <input
                onChange={updateInput}>
            </input>
            <button type="submit">Add Todo</button>
        </form>
    )
}

export default AddTodo
