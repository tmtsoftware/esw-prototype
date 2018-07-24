import * as React from 'react';

class HeaderComponent extends React.Component {

    public render() {
        return (
            <div>
                <div className="top-bar">
                    <span className="brand-title">TMT</span>
                    <span className="brand-tagline">Sequencer Component</span>
                </div>
                <a className="anchor" href="https://github.com/tmtsoftware/esw-prototype">Github</a>
            </div>
        );
    }
}

export default HeaderComponent;
