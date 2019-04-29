import React, {useState} from "react";

const e = () =>{}

export const InstrumentCalibrationMirrorContext = React.createContext({isUp: true, moveUp: e, moveDown: e, toggleMirror: e})

const InstrumentCalibrationMirrorContextProvider = (props) => {
    const [isUp, set] = useState(true)

    const moveUp = () => set(true)
    const moveDown = () => set(false)
    const toggleMirror = () => set(!isUp)

    return <InstrumentCalibrationMirrorContext.Provider value = {{isUp, moveUp, moveDown, toggleMirror}}>
        {props.children}
    </InstrumentCalibrationMirrorContext.Provider>
}

export default InstrumentCalibrationMirrorContextProvider