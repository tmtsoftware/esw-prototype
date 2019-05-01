import React from "react";
import PropTypes from "prop-types";
import posed from 'react-pose';
import styled from "styled-components";
import {Clickable} from "./Clickable";

function getX2Y2(x, y, length, angle) {
    const bdashX = length * Math.cos(angle * Math.PI / 180)
    const bdashY = length * Math.sin(angle * Math.PI / 180)
    const x2 = bdashX + x
    const y2 = bdashY + y
    return {x2, y2}
}

const PosedLine = posed.line({
    default: {
        x1: (props) => props.x1,
        y1: (props) => props.y1,
        x2: (props) => props.x2,
        y2: (props) => props.y2
    }
})

export const MirrorComponent = (props) => {
    const {midX, midY, angle, color, length, width, onClick, toolTip} = {...props};
    const x1 = midX - ((length / 2) * Math.cos(angle * Math.PI / 180))
    const y1 = midY - ((length / 2) * Math.sin(angle * Math.PI / 180))
    const {x2, y2} = getX2Y2(x1, y1, length, angle)
    const poseKey = `${x1}-${y1}-${x2}-${y2}`
    return <Clickable onClick={onClick} toolTip={toolTip}>
        <PosedLine pose={'default'}
                   poseKey={poseKey}
                   className={props.className}
                   x1={x1} y1={y1} x2={x2} y2={y2}
                   stroke={color}
                   strokeWidth={width}/>
    </Clickable>
};

export const Mirror = styled(MirrorComponent)`
cursor: pointer;
`

Mirror.defaultProps = {
    length: 80,
    angle: 45,
    color: "black",
    width: 3,
};

Mirror.propTypes = {
    midX: PropTypes.number.isRequired,
    midY: PropTypes.number.isRequired,
    length: PropTypes.number,
    angle: PropTypes.number,
    color: PropTypes.string,
    width: PropTypes.number,
    onClick: PropTypes.func,
    toolTip: PropTypes.string
};
