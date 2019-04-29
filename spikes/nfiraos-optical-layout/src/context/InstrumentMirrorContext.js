import React, {useState} from "react";

const e = () => {}
export const InstrumentMirrorContext = React.createContext({port: "bottom", setTopPort:e, setBottomPort:e, setSidePort: e})

const InstrumentMirrorContextProvider = (props) => {
    const [port, setPort] = useState("bottom")

    const setTopPort = () => setPort("top")
    const setBottomPort = () => setPort("bottom")
    const setSidePort = () => setPort("side")

    return <InstrumentMirrorContext.Provider value = {{port, setTopPort, setBottomPort, setSidePort}}>
        {props.children}
    </InstrumentMirrorContext.Provider>
}

export default InstrumentMirrorContextProvider