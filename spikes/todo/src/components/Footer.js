import React from 'react'
import Link from '../components/Link'
import { VisibilityFilters } from '../constants/visibilityFilters'

const Footer = () => {
    console.log('-----------> Footer')
    return <div>
        <span>Show: </span>
        <Link filter={VisibilityFilters.SHOW_ALL}>All</Link>
        <Link filter={VisibilityFilters.SHOW_ACTIVE}>Active</Link>
        <Link filter={VisibilityFilters.SHOW_COMPLETED}>Completed</Link>
    </div>
}

export default Footer
