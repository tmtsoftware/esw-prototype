import React from 'react'
import Footer from './Footer'
import AddTodo from '../components/AddTodo'
import VisibleTodoList from '../components/TodoList'
import Notification from '../components/Notification'
import TodosContextProvider from '../context/TodosContextProvider'
import VisibilityFilterContextProvider from "../context/VisibilityFilterContextProvider";
import NotificationContextProvider from "../context/NotificationContextProvider";

const App = () => (
    <div>
        <NotificationContextProvider>
        <VisibilityFilterContextProvider>
            <TodosContextProvider>
                <AddTodo/>
                <VisibleTodoList/>
                <Footer/>
            </TodosContextProvider>
        </VisibilityFilterContextProvider>
            <Notification/>
        </NotificationContextProvider>
    </div>
)

export default App
