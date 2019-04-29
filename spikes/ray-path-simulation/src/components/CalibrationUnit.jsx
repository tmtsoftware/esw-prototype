import React, {useContext} from "react";
import {TextBox} from "./TextBox";
import {DashBox} from "./DashBox";
import {Mirror} from "./Mirror";
import {InstrumentCalibrationMirrorContext} from "../context/InstrumentCalibrationMirrorContext";

export const CalibrationUnit = () => {
    const {isUp} = useContext(InstrumentCalibrationMirrorContext)
    const width = 150;
    const strokeWidth = 2;
    const height = 500;

    const mirrorMidY = isUp ? height/2 : height/2 + height/6

    return <svg x={"80"}>
        <DashBox backgroundColor="#00feff" x={2} y={2} width={width} height={height}>
            <TextBox x={width * 0.1 + strokeWidth} y={height * 0.03} width={width * 0.8} height={height * 0.12}>
                NFIRAOS Science Calibration Unit
            </TextBox>
            <TextBox width={width * 0.8}
                     height={height * 0.12}
                     y={height * 0.18}
                     x={width * 0.1 + strokeWidth}
                     color="white"
                     backgroundColor="#0433FF">
                NSCU arcs & flats
            </TextBox>
            <Mirror midX={width/2} midY={mirrorMidY} />
        </DashBox>
    </svg>
}