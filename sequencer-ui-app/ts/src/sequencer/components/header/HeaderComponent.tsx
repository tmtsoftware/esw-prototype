import * as React from 'react';

class HeaderComponent extends React.Component {

    public render() {
        return (
            <div>
                <div className="TopBarCss">
                    <span className="BrandTitleCss">TMT</span>
                    <span className="BrandTaglineCss">Sequencer Component</span>
                </div>
                <a className="AnchorCss" href="https://github.com/tmtsoftware/esw-prototype">Github</a>
            </div>
        );
    }
}

export default HeaderComponent;
