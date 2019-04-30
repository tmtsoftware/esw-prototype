import React from "react";
import {DashBox} from "./common/DashBox";
import {TextBox} from "./common/TextBox";
import PropTypes from "prop-types";
import {PinholeMask} from "./PinholeMask";

export const SourcesAndCalibration = (props) => {
    const width = props.width;
    const strokeWidth = 2;
    const height = 500;

    return <svg x={props.x}>

        <DashBox width={width} x={2} y={2} height={height}>
            <TextBox x={width * 0.1 + strokeWidth} y={height * 0.03} width={width * 0.8} height={height * 0.12} color="#FF2804">
                Sources and calibration
            </TextBox>
            
            <TextBox width={width/2 - 30} height={height/6} y={height * 0.15} x={strokeWidth + 20} backgroundColor="#FF5200" >
                NFIRAOS Calibration NGS sources
            </TextBox>

            <TextBox width={width/2 - 30 } height={height/6} y={height * 0.15} x={width/2+strokeWidth + 10} backgroundColor="#FF9200">
                NFIRAOS Calibration LGS sources
            </TextBox>

            <PinholeMask x={20} width={5} />

            <TextBox width={width * 0.4} height={height * 0.12} y={height * 0.80} x={0}>
                Pinhole Mask
            </TextBox>
        </DashBox>
    </svg>
};

SourcesAndCalibration.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired

};
