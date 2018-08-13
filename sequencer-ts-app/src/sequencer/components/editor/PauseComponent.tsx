import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";

interface IState {
    pauseResponse: string
}

interface IProps {
    client: EditorClient
}


class PauseComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {pauseResponse: ""};
    }

    public callBack(res: string) {
        this.setState({pauseResponse: res});
    }

    public render() {
        return (
            <div>
                <h6>Sequence Editor Pause</h6>
                <div>
                    <a className="btn-large">
                    <i className="material-icons" onClick={this.handleClick}>pause</i>
                    </a>
                    <span>{this.state.pauseResponse}</span>
                </div>
            </div>
        );
    }

    private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        this.props.client.pause((res) => this.callBack(res))
    };
}

export default PauseComponent;