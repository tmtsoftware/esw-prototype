import React from "react";
import {DashBox} from "../common/DashBox";
import {TextBox} from "../common/TextBox";
import {Mirror} from "../common/Mirror";

export const AlignmentTelescope = () => {
    const
    const width = 120;
    const strokeWidth = 2;
    const height = 500;
    const mirrorMidY = isUp ? height/2 : height/2 + height/6
    return <svg x={240}>
        <DashBox width={width} x={2} y={2} height={height}/>
        <TextBox x={width * 0.1 + strokeWidth} y={height * 0.03} width={width * 0.8} height={height * 0.12}>
            Alignment Telescope
        </TextBox>
        <TextBox width={width * 0.6}
                 height={height * 0.18}
                 y={height * 0.18}
                 x={width * 0.2 + strokeWidth}
                 backgroundColor="#BFBFFF">
        </TextBox>
        <Mirror midX={width/2} midY={mirrorMidY} />
        <TextBox width={width * 0.8} height={height * 0.12} y={height * 0.80} x={width * 0.1 + strokeWidth}>
            Alignment Telescope fold Mirror
        </TextBox>
    </svg>
};
