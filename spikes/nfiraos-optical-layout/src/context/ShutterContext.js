import React, {useState} from "react";

const e = () => {}
export const ShutterContext = React.createContext({open: false, openShutter:e, closeShutter:e, toggleShutter: e})

const ShutterContextProvider = (props) => {
    const [open, setOpen] = useState(false)

    const openShutter = () => setOpen(true)
    const closeShutter = () => setOpen(false)
    const toggleShutter = () => setOpen(!open)

    return <ShutterContext.Provider value = {{open, openShutter, closeShutter, toggleShutter}}>
        {props.children}
    </ShutterContext.Provider>
}

export default ShutterContextProvider