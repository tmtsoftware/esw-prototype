import posed from "react-pose";
import PropTypes from "prop-types";

const getH = (value) => {
    const pattern = /.* h(\d+)/gm
    return parseInt(pattern.exec(value)[1])
}

const getL = (value) => {
    const pattern = /l(-?)(\d+)/gm
    return parseInt(pattern.exec(value)[2])
}

export const PosedPath = posed.path({
    default: {
        d: (props) => props.d,
        transition: ({from, to}) => {

            const isComingFromPort = getL(from) !== 0
            const isGoingToPort = getL(to) !== 0
            const isBackwardMotion = () => getH(from) > getH(to)
            const isPortToPort = isGoingToPort && isComingFromPort

            let corner = "m10,10 h1083 l0,0";
            return ({
            type: 'keyframes',
            easing: 'linear',
            values: [from, (isPortToPort || !isGoingToPort) ? to : corner ,to],
            // values: [from, to],
            times: [0, 0.7, 1],
            duration: isBackwardMotion() ? 0 : 300,
        })}
    }
})


PosedPath.propTypes= {
    d: PropTypes.string.isRequired
}