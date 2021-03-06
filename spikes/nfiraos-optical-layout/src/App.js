import React from 'react';
import './App.css';
import {CalibrationUnit} from "./components/CalibrationUnit";
import MainShutterContextProvider from "./context/MainShutterContext";
import PinholeMaskContextProvider from "./context/PinholeMaskContext";
import InstrumentCalibrationMirrorContextProvider from "./context/InstrumentCalibrationMirrorContext";
import {AlignmentTelescope} from "./components/AlignmentTelescope";
import AlignmentMirrorContextProvider from "./context/AlignmentMirrorContext";
import {SourcesAndCalibration} from "./components/SourcesAndCalibration";
import {MainShutter} from "./components/MainShutter";
import {InstrumentSelectionMirror} from "./components/InstrumentSelectionMirror";
import InstrumentMirrorContextProvider from "./context/InstrumentMirrorContext";
import ReactTooltip from 'react-tooltip';
import {MainLight} from "./components/MainLight";

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

        const instrumentMirrorX = sourceAndCalibX + sourceAndCalibWidth + gap
        const instrumentMirrorWidth = 520
        return (
            <MainShutterContextProvider>
                <InstrumentCalibrationMirrorContextProvider>
                    <AlignmentMirrorContextProvider>
                        <PinholeMaskContextProvider>
                            <InstrumentMirrorContextProvider>
                                <div className="App">
                                    <svg viewBox="0 0 1250 550" width="1250" height="550">
                                        <MainShutter x={shutterX} width={shutterWidth}/>
                                        <CalibrationUnit x={calibrationUnitX} width={calibrationWidth}/>
                                        <AlignmentTelescope x={alignmentUnitX} width={alignmentWidth}/>
                                        <SourcesAndCalibration x={sourceAndCalibX} width={sourceAndCalibWidth}/>
                                        <InstrumentSelectionMirror x={instrumentMirrorX}
                                                                   width={instrumentMirrorWidth}/>
                                        <MainLight x={x} initialWidth={lightWidth}/>
                                    </svg>
                                    <ReactTooltip effect={'solid'} />
                                </div>
                            </InstrumentMirrorContextProvider>
                        </PinholeMaskContextProvider>
                    </AlignmentMirrorContextProvider>
                </InstrumentCalibrationMirrorContextProvider>
            </MainShutterContextProvider>
        );
    }
}

export default App;
