import React, {useState} from "react";
import posed from 'react-pose';
import {useInterval} from './UseInterval'

const Line = posed.line({
    default: {
        x1: (props) => props.x1,
        y1: (props) => props.y1,
        x2: (props) => props.x2,
        y2: (props) => props.y2
    }
})

export const AnimatedSvg = (props) => {

    const [up, setUp] = useState(true)

    useInterval(() => {
        setUp(!up)
    }, 1000)

    const x1 = up ? 0 : 100
    const x2 = up ? 100 : 0

    return <svg>
        <Line stroke={"black"}
              strokeWidth="4"
              pose={'default'}
              poseKey={up}
              x1={x1} y1={0} x2={x2} y2={100}
        />
    </svg>
};
