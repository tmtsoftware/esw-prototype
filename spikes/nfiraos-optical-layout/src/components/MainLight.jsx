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

    // this can be improved
    let coordinates = ["m10,10",
        mainShutter.open ? calibrationMirror.isUp ? "h128" : alignmentMirror.isUp ? "h273" : pinholeMask.open ? "h1083 " + getPortCoordinates() : "h363" : "h40"
    ]

    return <svg y={240}>
        <Arrow coordinates={coordinates.join(" ")} color={"blue"} />
    </svg>

};

MainLight.defaultProps = {};

MainLight.propTypes = {};