import React from "react";
import PropTypes from "prop-types";

export const DashBox = (props) => {
    const {x, y, width, height, backgroundColor, children} = {...props};
    return <g>
        <rect fill={backgroundColor === "transparent" ? "white" : backgroundColor}
              fillOpacity={backgroundColor === "transparent" ? 0 : 1}
              stroke="black"
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeDasharray="8.0"
              strokeWidth="2"
              x={x}
              y={y}
              width={width}
              height={height}
        />
        {children}
    </g>

};

DashBox.defaultProps = {
    backgroundColor: "transparent",
};

DashBox.propTypes = {
    x: PropTypes.number.isRequired,
    y: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired,
    height: PropTypes.number.isRequired,
    backgroundColor: PropTypes.string,
};