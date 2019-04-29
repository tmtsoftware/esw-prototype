import React, {useState} from "react";

const e = () =>{}

export const AlignmentMirrorContext = React.createContext({isUp: false, moveUp: e, moveDown: e, toggleAlignmentMirror: e})

const AlignmentMirrorContextProvider = (props) => {
    const [isUp, set] = useState(false)

    const moveUp = () => set(true)
    const moveDown = () => set(false)
    const toggleAlignmentMirror = () => set(!isUp)

    return <AlignmentMirrorContext.Provider value = {{isUp, moveUp, moveDown, toggleAlignmentMirror: toggleAlignmentMirror}}>
        {props.children}
    </AlignmentMirrorContext.Provider>
}

export default AlignmentMirrorContextProvider