import React from 'react';
import './App.css';
import {CalibrationUnit} from "./components/calibrationunit/CalibrationUnit";
import {Shutter} from "./components/shutter/Shutter";
import {Light} from "./components/Light";
import {ControlPanel} from "./components/controlpanel/ControlPanel";
import ShutterContextProvider from "./context/ShutterContext";
import InstrumentCalibrationMirrorContextProvider from "./context/InstrumentCalibrationMirrorContext";
import {AlignmentTelescope} from "./components/alignmenttelescope/AlignmentTelescope";

class App extends React.Component {
    render() {
        return (
            <ShutterContextProvider>
                <InstrumentCalibrationMirrorContextProvider>
                    <div className="App">
                        <ControlPanel />
                        <svg viewBox="0 0 1024 600" width="1024" height="600">
                            <Shutter/>
                            <CalibrationUnit/>
                            <AlignmentTelescope/>
                            <Light/>
                        </svg>
                    </div>
                </InstrumentCalibrationMirrorContextProvider>
            </ShutterContextProvider>
        );
    }
}

export default App;
