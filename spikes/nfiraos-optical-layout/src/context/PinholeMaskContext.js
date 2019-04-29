import React, {useState} from "react";

const e = () => {}
export const PinholeMaskContext = React.createContext({open: false, openShutter:e, closeShutter:e, toggleShutter: e})

const PinholeMaskProvider = (props) => {
    const [open, setOpen] = useState(false)

    const openShutter = () => setOpen(true)
    const closeShutter = () => setOpen(false)
    const toggleShutter = () => setOpen(!open)

    return <PinholeMaskContext.Provider value = {{open, openShutter, closeShutter, toggleShutter}}>
        {props.children}
    </PinholeMaskContext.Provider>
}

export default PinholeMaskProvider