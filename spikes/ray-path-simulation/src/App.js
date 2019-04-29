import React from 'react';
import './App.css';
import {CalibrationUnit} from "./components/CalibrationUnit";
import {Shutter} from "./components/Shutter";
import {Light} from "./components/Light";
import {ControlPanel} from "./components/ControlPanel";
import ShutterContextProvider from "./context/ShutterContext";
import InstrumentCalibrationMirrorContextProvider from "./context/InstrumentCalibrationMirrorContext";

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
                            <Light/>
                        </svg>
                    </div>
                </InstrumentCalibrationMirrorContextProvider>
            </ShutterContextProvider>
        );
    }
}

export default App;
