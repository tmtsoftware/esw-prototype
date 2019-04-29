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
    const portToAngle = {
        "top": 60,
        "bottom": 45,
        "side": 30
    }

    const f = (baseX) => baseX + portWidth + portGap

    const IRMSX = 180
    const IRISX = f(IRMSX) + 15
    const WFSX = f(IRISX) + 15
    const CAMX = f(WFSX)
    return <svg x={props.x}>


        <DashBox width={width} x={2} y={2} height={height}>
            <TextBox width={portWidth} height={70} y={400} x={IRMSX} backgroundColor="#D782FF" onClick={setTopPort}>
                IRMS
            </TextBox>
            <TextBox width={portWidth} height={70} y={400} x={IRISX} backgroundColor="#D782FF" onClick={setBottomPort}>
                IRIS
            </TextBox>
            <TextBox width={portWidth} height={70} y={400} x={WFSX} backgroundColor="#D782FF" onClick={setSidePort}>
                HiRES WFS
            </TextBox>
            <TextBox width={portWidth} height={70} y={400} x={CAMX} backgroundColor="#D782FF" onClick={setSidePort}>
                Acq. Cam.
            </TextBox>

            <Mirror midY={height / 2} midX={f(IRISX) + 70} angle={portToAngle[port]}/>

        </DashBox>
    </svg>
};

InstrumentSelectionMirror.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired
};


