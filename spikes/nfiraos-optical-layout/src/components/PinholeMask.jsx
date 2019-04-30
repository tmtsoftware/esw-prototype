import React, {useContext} from "react";
import PropTypes from "prop-types";
import {Shutter} from "./common/Shutter";
import {PinholeMaskContext} from "../context/PinholeMaskContext";

export const PinholeMask = (props) => {
    const {open, toggleShutter} = useContext(PinholeMaskContext)
    return <Shutter color="#FF9200" open={open} {...props} onClick={toggleShutter}/>
}

PinholeMask.propTypes = {
    x: PropTypes.number.isRequired,
    width: PropTypes.number.isRequired
};
