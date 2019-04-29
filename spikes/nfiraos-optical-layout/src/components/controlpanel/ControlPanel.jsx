import React, {useContext} from "react";
import './ControlPanel.css'
import {MainShutterContext} from "../../context/MainShutterContext";
import {PinholeMaskContext} from "../../context/PinholeMaskContext";
import {InstrumentCalibrationMirrorContext} from "../../context/InstrumentCalibrationMirrorContext";
import {AlignmentMirrorContext} from "../../context/AlignmentMirrorContext";

export const ControlPanel = () => {
    const {open, toggleShutter} = useContext(MainShutterContext)
    const pinholeMaskContext = useContext(PinholeMaskContext)
    const {isUp, toggleMirror} = useContext(InstrumentCalibrationMirrorContext)
    const {isAlignmentMirrorUp, toggleAlignmentMirror} = useContext(AlignmentMirrorContext)

    return <div id="control-panel">
        <div id="panel-header">
            Control Panel
        </div>
        <div id="panel-body">
            <button onClick={toggleShutter} className="panel-button">
                {open === true ? "Close Shutter" : "Open Shutter"}
            </button>
            <button onClick={toggleMirror} className="panel-button">
                {isUp === true ? "Move Instrument Calibration Mirror down" : "Move Instrument Calibration Mirror up"}
            </button>
            <button onClick={toggleAlignmentMirror} className="panel-button">
                {isAlignmentMirrorUp === true ? "Move Alignment Mirror down" : "Move Alignment Mirror up"}
            </button>
            <button onClick={pinholeMaskContext.toggleShutter} className="panel-button">
                {pinholeMaskContext.open === true ? "Close Pinhole Mask" : "Open Pinhole Mask"}
            </button>
        </div>
    </div>
};
