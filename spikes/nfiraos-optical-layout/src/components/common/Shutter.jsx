import React from "react";
import './Shuter.css'
import PropTypes from "prop-types";

export const Shutter = (props) => {
    const topOffSet = props.open === true ? 150 : 200;
    const bottomOffSet = props.open === true ? 300 : 250;
    return <svg x={props.x}>
        /*TOP SHUTTER*/
        <path id={"top-shutter"} d={`m1 ${topOffSet} v50`}
              stroke={props.color}
              strokeWidth={props.width}/>

        /*BOTTOM SHUTTER*/
        <path id={"bottom-shutter"} d={`m1 ${bottomOffSet} v50`}
              stroke={props.color}
              strokeWidth={props.width}/>
    </svg>
}

Shutter.defaultProps={
    color: "black"
}

Shutter.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired,
    color: PropTypes.string,
    open: PropTypes.bool.isRequired
};
