import React, {useState} from 'react'
import NotificationContext from './NotificationContext'

const NotificationContextProvider = (props) => {
    const [notificationText, setNotificationText] = useState("")
    const showNotification = (text) => {
        setNotificationText(text)
    }

    return <NotificationContext.Provider value={
        {
            notificationText,
            showNotification
        }
    }>
        {props.children}
    </NotificationContext.Provider>
}

export default NotificationContextProvider
