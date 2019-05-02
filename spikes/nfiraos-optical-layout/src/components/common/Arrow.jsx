import React from "react";
import PropTypes from "prop-types";
import {PosedPath} from "../internals/PosedPath";

export const Arrow = (props) => {
    const poseKey = Date()

    return <svg>
        <defs>
            /*ARROW END*/
            <marker id="arrow"
                    markerWidth="4"
                    markerHeight="4"
                    refX={"0"}
                    refY={"2"}
                    orient={"auto"}
                    markerUnits={"strokeWidth"}>
                <polygon points="0,0 0,4 4,2" fill={props.color}/>
            </marker>
        </defs>
        /*ARROW*/
        <PosedPath id={"light"}
                   poseKey={poseKey}
                   pose={'default'}
                   d={props.coordinates}
                   fillOpacity={0}
                   stroke={props.color}
                   strokeWidth={props.width}
                   markerEnd="url(#arrow)"
        />
    </svg>
}

Arrow.defaultProps = {
    color: "black",
    width: 3
}

Arrow.propTypes = {
    coordinates: PropTypes.string.isRequired,
    color: PropTypes.string,
    width: PropTypes.number
}