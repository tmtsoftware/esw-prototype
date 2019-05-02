import posed from "react-pose";
import PropTypes from "prop-types";

export const PosedPath = posed.path({
    default: {
        d: (props) => props.d
    }
})

PosedPath.propTypes= {
    d: PropTypes.string.isRequired
}