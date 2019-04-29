import React from 'react';
import './App.css';
import {CalibrationUnit} from "./components/CalibrationUnit";
import {Light} from "./components/Light";
import {ControlPanel} from "./components/controlpanel/ControlPanel";
import MainShutterContextProvider from "./context/MainShutterContext";
import PinholeMaskContextProvider from "./context/PinholeMaskContext";
import InstrumentCalibrationMirrorContextProvider from "./context/InstrumentCalibrationMirrorContext";
import {AlignmentTelescope} from "./components/AlignmentTelescope";
import AlignmentMirrorContextProvider from "./context/AlignmentMirrorContext";
import {SourcesAndCalibration} from "./components/SourcesAndCalibration";
import {MainShutter} from "./components/MainShutter";

class App extends React.Component {
    render() {
        const gap = 10

        const x = 0
        const lightWidth = 40
        const markerWidth = 20

        const shutterX = x + lightWidth + markerWidth
        const shutterWidth = 5

        const calibrationUnitX = shutterX + shutterWidth + gap
        const calibrationWidth = 150

        const alignmentUnitX = calibrationUnitX + calibrationWidth + gap
        const alignmentWidth = 120

        const sourceAndCalibX = alignmentUnitX + alignmentWidth + gap
        const sourceAndCalibWidth = 320

        return (
            <MainShutterContextProvider>
                <InstrumentCalibrationMirrorContextProvider>
                    <AlignmentMirrorContextProvider>
                        <PinholeMaskContextProvider>
                            <div className="App">
                                <ControlPanel/>
                                <svg viewBox="0 0 1024 600" width="1024" height="600">
                                    <MainShutter x={shutterX} width={shutterWidth}/>
                                    <CalibrationUnit x={calibrationUnitX} width={calibrationWidth}/>
                                    <AlignmentTelescope x={alignmentUnitX} width={alignmentWidth}/>
                                    <SourcesAndCalibration x={sourceAndCalibX} width={sourceAndCalibWidth}/>
                                    <Light x={x} initialWidth={lightWidth}/>

                                </svg>
                            </div>
                        </PinholeMaskContextProvider>
                    </AlignmentMirrorContextProvider>
                </InstrumentCalibrationMirrorContextProvider>
            </MainShutterContextProvider>
        );
    }
}

export default App;
