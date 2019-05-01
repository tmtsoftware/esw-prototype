import React from "react";
import PropTypes from "prop-types";

export const Clickable = (props) => {
    const {children, toolTip, onClick} = {...props};
    return <g
        style={onClick ? {cursor: 'pointer'} : {}}
        onClick={onClick}
        data-tip={toolTip}>
        {children}
    </g>
};

Clickable.defaultProps = {

};

Clickable.propTypes = {
    toolTip: PropTypes.string,
    onClick: PropTypes.func
};