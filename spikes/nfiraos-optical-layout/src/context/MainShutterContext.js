import React, {useState} from "react";

const e = () => {}
export const MainShutterContext = React.createContext({open: false, openShutter:e, closeShutter:e, toggleShutter: e})

const MainShutterContextProvider = (props) => {
    const [open, setOpen] = useState(false)

    const openShutter = () => setOpen(true)
    const closeShutter = () => setOpen(false)
    const toggleShutter = () => setOpen(!open)

    return <MainShutterContext.Provider value = {{open, openShutter, closeShutter, toggleShutter}}>
        {props.children}
    </MainShutterContext.Provider>
}

export default MainShutterContextProvider