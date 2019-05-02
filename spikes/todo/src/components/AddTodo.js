import React, {useContext} from "react";
import TodosContext from "../context/TodosContext";

const AddTodo = () => {
    const context = useContext(TodosContext)

    console.log("----------> rendering AddTodo")
    return (
        <form
            onSubmit={
                (e) => {
                    e.preventDefault()
                    const input = document.getElementById("addTodo").value
                    context.addTodo(input)
                }
            }>
            <input id="addTodo">
            </input>
            <button type="submit">Add Todo</button>
        </form>
    )
}

export default AddTodo
