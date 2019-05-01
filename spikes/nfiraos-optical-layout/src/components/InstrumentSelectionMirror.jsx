import React, {useContext} from "react";
import {DashBox} from "./common/DashBox";
import {TextBox} from "./common/TextBox";
import PropTypes from "prop-types";
import {Mirror} from "./common/Mirror";
import {InstrumentMirrorContext} from "../context/InstrumentMirrorContext";

export const InstrumentSelectionMirror = (props) => {
    const {port, setTopPort, setBottomPort, setSidePort} = useContext(InstrumentMirrorContext)
    const width = props.width
    const height = 500;
    const portWidth = 60
    const portGap = 10

    const data = [
        {angle: 60, action: setTopPort},
        {angle: 45, action: setBottomPort},
        {angle: 30, action: setSidePort}
    ]

    const portToIndex = {
        "top": 0,
        "bottom": 1,
        "side": 2
    }

    const nextIndex = () => {
        const currentIndex = portToIndex[port]
        return (currentIndex === 2) ? 0 : currentIndex + 1
    }

    const f = (baseX) => baseX + portWidth + portGap

    const IRMSX = 180
    const IRISX = f(IRMSX) + 15
    const WFSX = f(IRISX) + 15
    const CAMX = f(WFSX)

    const onClickHandler = data[nextIndex()].action

    return <svg x={props.x}>
        <DashBox width={width} x={2} y={2} height={height}>

            <Mirror toolTip={"change position"}
                    midY={height / 2}
                    midX={f(IRISX) + 70}
                    onClick={onClickHandler}
                    angle={data[portToIndex[port]].angle}
            />

            <TextBox width={portWidth} height={70} y={400} x={IRMSX} backgroundColor="#D782FF"
                     toolTip={'Point mirror here'}
                     clickable={true}
                     onClick={setTopPort}>
                IRMS
            </TextBox>

            <TextBox width={portWidth} height={70} y={400} x={IRISX} backgroundColor="#D782FF"
                     toolTip={'Point mirror here'}
                     clickable={true}
                     onClick={setBottomPort}>
                IRIS
            </TextBox>
            <TextBox width={portWidth} height={70} y={400} x={WFSX} backgroundColor="#D782FF"
                     toolTip={'Point mirror here'}
                     clickable={true}
                     onClick={setSidePort}>
                HiRES WFS
            </TextBox>
            <TextBox width={portWidth} height={70} y={400} x={CAMX} backgroundColor="#D782FF"
                     toolTip={'Point mirror here'}
                     clickable={true}
                     onClick={setSidePort}>
                Acq. Cam.
            </TextBox>
        </DashBox>
    </svg>
};

InstrumentSelectionMirror.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired
};


