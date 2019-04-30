import React, {useContext} from "react";
import PropTypes from "prop-types";
import {Shutter} from "./common/Shutter";
import {MainShutterContext} from "../context/MainShutterContext";

export const MainShutter = (props) => {
    const {open,toggleShutter} = useContext(MainShutterContext)
    return <Shutter color="red" open={open} {...props} onClick={toggleShutter} />
}

MainShutter.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired
};
