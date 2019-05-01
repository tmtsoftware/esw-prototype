import React from "react";
import PropTypes from "prop-types";
import {Text} from "../internals/Text";
import {Clickable} from "../internals/Clickable";

export const TextBox = (props) => {
    const {x, y, width, height, color, backgroundColor, onClick, children, toolTip} = {...props};

    return <Clickable onClick={onClick} toolTip={toolTip} >
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
    </Clickable>
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
    children: PropTypes.string,
    onClick: PropTypes.func,
    toolTip: PropTypes.string
};