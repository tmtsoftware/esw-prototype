import React, {useState} from "react";
import posed from 'react-pose';
import styled from 'styled-components';
import {useInterval} from './UseInterval'

const Box = posed.div({
    hidden: {
        marginLeft: (props) => props.offset,
        opacity: 0
    },
    visible: {
        marginLeft: (props) => props.offset,
        opacity: 1,
        transition: { type: 'spring', stiffness: 100 }
    }
})

const StyledBox = styled(Box)`
    width:100px;
    height: 100px;
    background: indianred;
`

export const AnimatedBox = (props) => {

    const [isVisible, setVisible] = useState(true)

    useInterval(() => {
        setVisible(!isVisible)
    }, 1000)

    return <StyledBox offset={isVisible? 50 : -100} pose={isVisible ? 'visible' : 'hidden'}/>
};

