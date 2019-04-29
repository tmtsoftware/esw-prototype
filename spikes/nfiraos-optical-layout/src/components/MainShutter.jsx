import React, {useContext} from "react";
import PropTypes from "prop-types";
import {Shutter} from "./common/Shutter";
import {MainShutterContext} from "../context/MainShutterContext";

export const MainShutter = (props) => {
    const open = useContext(MainShutterContext).open
    return <Shutter color="red" open={open} {...props}/>
}

MainShutter.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired
};
