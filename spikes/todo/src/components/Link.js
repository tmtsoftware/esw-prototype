import React, {useContext} from 'react'
import PropTypes from 'prop-types'
import VisibilityFilterContext from "../context/VisibilityFilterContext";


const Link = (props) => {
    const context = useContext(VisibilityFilterContext)

    return (
        <button
            onClick={() => context.setVisibilityFilter(props.filter)}
            disabled={props.filter === context.visibilityFilter}
            style={{
                marginLeft: '4px'
            }}
        >
            {props.children}
        </button>
    )

}

Link.propTypes = {
    filter: PropTypes.string.isRequired,
    children: PropTypes.node.isRequired,
}

export default Link
