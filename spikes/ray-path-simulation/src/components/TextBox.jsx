import React from "react";
import PropTypes from "prop-types";
import {Text} from "./Text";

export const TextBox = (props) => {
    const {x, y, width, height, color, backgroundColor, children} = {...props};
    return <g>
        <rect
            fill={backgroundColor === "transparent" ? "white" : backgroundColor}
            fillOpacity={backgroundColor === "transparent" ? 0 : 1}
            stroke={backgroundColor === "transparent" ? "" : "black"}
            strokeWidth="1"
            height={height}
            width={width}
            x={x}
            y={y}
        />

        <Text x={x} y={y} width={width} height={height} color={color}>
            {children}
        </Text>
    </g>
};

TextBox.defaultProps = {
    color: "black",
    backgroundColor: "transparent",
};

TextBox.propTypes = {
    x: PropTypes.number.isRequired,
    y: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired,
    height: PropTypes.number.isRequired,
    color: PropTypes.string,
    backgroundColor: PropTypes.string,
    children: PropTypes.string
};