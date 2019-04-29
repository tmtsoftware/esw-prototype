import React from "react";
import PropTypes from "prop-types";

export class Text extends React.Component{
    render() {
        const {x,y,width, height, color, children} = {...this.props}
        return <foreignObject x={x} y={y} width={width} height={height}>
            <div xmlns="http://www.w3.org/1999/xhtml" style={
                {
                    textAlign:"center",
                    color: color,
                    padding: "0.1em",
                    height: "100%",
                    display: "flex",
                    alignItems: "center"
                }
            }>
                <div>{children}</div>
            </div>
        </foreignObject>
    }
}

Text.defaultProps = {
    color: "black"
};

Text.propTypes = {
    x: PropTypes.number.isRequired,
    y: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired,
    height: PropTypes.number.isRequired,
    color: PropTypes.string,
    children: PropTypes.string.isRequired
};