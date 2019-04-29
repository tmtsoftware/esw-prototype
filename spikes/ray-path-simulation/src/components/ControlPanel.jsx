import React, {useContext} from "react";
import './ControlPanel.css'
import {ShutterContext} from "../context/ShutterContext";
import {InstrumentCalibrationMirrorContext} from "../context/InstrumentCalibrationMirrorContext";

export const ControlPanel = () => {
    const {open, toggleShutter} = useContext(ShutterContext)
    const {isUp, toggleMirror} = useContext(InstrumentCalibrationMirrorContext)

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
        </div>
    </div>
};
