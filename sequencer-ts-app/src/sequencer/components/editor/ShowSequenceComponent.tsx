import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";
import {cssClasses} from "../../jss/customstyles";

interface IState {
    showSequenceResponse: string
}

interface IProps {
    client: EditorClient
}

class ShowSequenceComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {showSequenceResponse: ""};
    }

    public callBack(res: string) {
        this.setState({showSequenceResponse: res});
    }

    public render() {
        return (
            <div className="card-panel hoverable">
                <h6>Sequence Editor Show Sequence</h6>
                <div>
                    <button className={`${cssClasses.button}`} onClick={this.handleClick}>Show Sequence</button>
                    <div><span><textarea className={`${cssClasses.textArea}`} value={this.state.showSequenceResponse}/></span></div>
                </div>
            </div>
        );
    }

    private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        this.props.client.showSequence((res) => this.callBack(res))
    };
}

export default ShowSequenceComponent;