import React, {useContext} from 'react'
import NotificationContext from '../context/NotificationContext'

const Notification = () => {
    const context = useContext(NotificationContext)

    console.log("----------> rendering Notification")
    return <div>
        {context.notificationText}
        <button onClick={() => context.showNotification("")}>Hide notification</button>
    </div>
}

export default Notification
