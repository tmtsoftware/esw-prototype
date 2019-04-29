import React from "react";
import PropTypes from "prop-types";

function getB(x,y, length, angle) {
    const bdashX = length * Math.cos(angle * Math.PI / 180)
    const bdashY = length * Math.sin(angle* Math.PI / 180)
    const x2 = bdashX + x
    const y2 = bdashY + y
    return {x2, y2}

}

export const Mirror = (props) => {
    const {midX, midY, angle, color, length, width} = {...props};
    const x1 = midX - ((length / 2) * Math.cos(angle * Math.PI / 180) )
    const y1 = midY - ((length / 2) * Math.sin(angle * Math.PI / 180))
    const B = getB(x1,y1,length, angle)
    return <line x1={x1} y1={y1} x2={B.x2} y2={B.y2} stroke={color} strokeWidth={width} />
};

Mirror.defaultProps = {
    length: 80,
    angle: 45,
    color: "black",
    width: 3
};

Mirror.propTypes = {
    midX: PropTypes.number.isRequired,
    midY: PropTypes.number.isRequired,
    length: PropTypes.number,
    angle: PropTypes.number,
    color: PropTypes.string,
    width: PropTypes.number,
};
