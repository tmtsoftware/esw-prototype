import React, {useContext} from "react";
// import PropTypes from "prop-types";
import {Arrow} from "./common/Arrow";
import {InstrumentCalibrationMirrorContext} from "../context/InstrumentCalibrationMirrorContext";
import {AlignmentMirrorContext} from "../context/AlignmentMirrorContext";
import {PinholeMaskContext} from "../context/PinholeMaskContext";
import {MainShutterContext} from "../context/MainShutterContext";
import {InstrumentMirrorContext} from "../context/InstrumentMirrorContext";

export const MainLight = (props) => {

    const mainShutter = useContext(MainShutterContext)
    const calibrationMirror = useContext(InstrumentCalibrationMirrorContext)
    const alignmentMirror = useContext(AlignmentMirrorContext)
    const pinholeMask = useContext(PinholeMaskContext)
    const instrumentMirror = useContext(InstrumentMirrorContext)

    const getPortCoordinates = () => {
        if(instrumentMirror.port === "top")
            return "l-180,135"
        else if(instrumentMirror.port === "bottom")
            return "l-95,135"
        else
            return "l13,135"
    }

    let move = "m10,10 "

    if(!mainShutter.open)
        move += "h40 l0,0"
    else if (calibrationMirror.isUp)
        move += "h128 l0,0"
    else if(alignmentMirror.isUp)
        move += "h273 l0,0"
    else if(pinholeMask.open)
        move += "h1083 " + getPortCoordinates()
    else
        move +="h363 l0,0"

    return <svg y={240}>
        <Arrow coordinates={move} color={"blue"} />
    </svg>

};

MainLight.defaultProps = {};

MainLight.propTypes = {};