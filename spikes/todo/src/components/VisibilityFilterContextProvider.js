import React, {useState} from 'react'
import VisibilityFilterContext from '../context/VisibilityFilterContext'
import {VisibilityFilters} from "../constants/visibilityFilters";

const VisibilityFilterContextProvider = (props) => {
    const [visibilityFilter, setVisibilityFilter] = useState(VisibilityFilters.SHOW_ALL)

    const setFilter = (filter) => {
        setVisibilityFilter(filter)
    }

    return (
        <VisibilityFilterContext.Provider value={
            {
                visibilityFilter,
                setVisibilityFilter: setFilter
            }
        }>
            {props.children}
        </VisibilityFilterContext.Provider>
    )
}

export default VisibilityFilterContextProvider
