import * as React from 'react';

class HeaderComponent extends React.Component {

    public render() {
        return (
            <nav>
                <div className="nav-wrapper indigo">
                    <a href="#" className="brand-logo">TMT</a>
                    <ul id="nav-mobile" className="right hide-on-med-and-down">
                        <li><a href="https://github.com/tmtsoftware/esw-prototype">Github</a></li>
                    </ul>
                </div>
            </nav>
        );
    }
}

export default HeaderComponent;
