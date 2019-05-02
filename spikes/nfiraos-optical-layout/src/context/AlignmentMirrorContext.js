import React, {useState} from "react";

const e = () =>{}

export const AlignmentMirrorContext = React.createContext({isUp: true, moveUp: e, moveDown: e, toggleAlignmentMirror: e})

const AlignmentMirrorContextProvider = (props) => {
    const [isUp, set] = useState(true)

    const moveUp = () => set(true)
    const moveDown = () => set(false)
    const toggleAlignmentMirror = () => set(!isUp)

    return <AlignmentMirrorContext.Provider value = {{isUp, moveUp, moveDown, toggleAlignmentMirror: toggleAlignmentMirror}}>
        {props.children}
    </AlignmentMirrorContext.Provider>
}

export default AlignmentMirrorContextProvider