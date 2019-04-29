import React from 'react'
import Footer from './Footer'
import AddTodo from '../components/AddTodo'
import VisibleTodoList from '../components/TodoList'
import TodosContextProvider from './TodosContextProvider'
import VisibilityFilterContextProvider from "./VisibilityFilterContextProvider";

const App = () => (
    <div>
        <VisibilityFilterContextProvider>
            <TodosContextProvider>
                <AddTodo/>
                <VisibleTodoList/>
            </TodosContextProvider>
            <Footer/>
        </VisibilityFilterContextProvider>
    </div>
)

export default App
