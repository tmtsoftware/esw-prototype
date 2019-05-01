import React from "react";
import './Shuter.css'
import PropTypes from "prop-types";
import styled from "styled-components";
import {Clickable} from "../internals/Clickable";

const ShutterComponent = (props) => {
    const topOffSet = props.open === true ? 150 : 200;
    const bottomOffSet = props.open === true ? 300 : 250;
    return <svg x={props.x} className={props.className}>
        /*TOP SHUTTER*/
        <Clickable toolTip={props.tooltip} onClick={props.onClick}>
            <path id={"top-shutter"} d={`m1 ${topOffSet} v50`}
                  stroke={props.color}
                  strokeWidth={props.width} onClick={props.onClick}/>
        </Clickable>

        <Clickable toolTip={props.tooltip} onClick={props.onClick}>
            /*BOTTOM SHUTTER*/
            <path id={"bottom-shutter"} d={`m1 ${bottomOffSet} v50`}
                  stroke={props.color}
                  strokeWidth={props.width} onClick={props.onClick}/>
        </Clickable>
    </svg>

}

export const Shutter = styled(ShutterComponent)`
    opacity:0.8;
    cursor: pointer;
   :hover{
      opacity:1;
   }
`;

Shutter.defaultProps = {
    color: "black"
}

Shutter.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired,
    color: PropTypes.string,
    open: PropTypes.bool.isRequired,
    onClick: PropTypes.func,
    tooltip: PropTypes.string
};
