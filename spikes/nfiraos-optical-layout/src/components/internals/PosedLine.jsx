import posed from "react-pose";

export const PosedLine = posed.line({
    default: {
        x1: (props) => props.x1,
        y1: (props) => props.y1,
        x2: (props) => props.x2,
        y2: (props) => props.y2
    }
})