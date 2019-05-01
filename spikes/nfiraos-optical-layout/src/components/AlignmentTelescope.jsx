import React, {useContext} from "react";
import {DashBox} from "./common/DashBox";
import {TextBox} from "./common/TextBox";
import {Mirror} from "./common/Mirror";
import {AlignmentMirrorContext} from "../context/AlignmentMirrorContext";
import PropTypes from "prop-types";

export const AlignmentTelescope = (props) => {
    const {isUp, toggleAlignmentMirror} = useContext(AlignmentMirrorContext)
    const width = props.width;
    const strokeWidth = 2;
    const height = 500;
    const mirrorMidY = isUp ? height/2 : height/2 + height/6
    return <svg x={props.x}>
        <DashBox width={width} x={2} y={2} height={height} >
            <TextBox x={width * 0.1 + strokeWidth} y={height * 0.03} width={width * 0.8} height={height * 0.12}>
                Alignment Telescope
            </TextBox>
            <TextBox width={width * 0.6}
                     height={height * 0.18}
                     y={height * 0.18}
                     x={width * 0.2 + strokeWidth}
                     backgroundColor="#BFBFFF">
            </TextBox>
            <Mirror toolTip={"move"} midX={width/2} midY={mirrorMidY} onClick={toggleAlignmentMirror} />
            <TextBox width={width * 0.8} height={height * 0.12} y={height * 0.80} x={width * 0.1 + strokeWidth}>
                Alignment Telescope fold Mirror
            </TextBox>
        </DashBox>
    </svg>
};

AlignmentTelescope.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired
};
