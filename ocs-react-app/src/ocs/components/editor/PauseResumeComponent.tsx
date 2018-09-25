import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";

interface IState {
    response: string,
    buttonText: string
}

interface IProps {
    client: EditorClient
}


class PauseResumeComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {response: "", buttonText: "pause"};
    }

    public callBack(res: string, buttonText: string) {
        this.setState({response: res, buttonText});
    }

    public render() {
        return (
            <div className="card-panel hoverable">
                <h6>Sequence Editor Pause/Resume</h6>
                <div>
                    <a className="btn-large indigo">
                    <i className="material-icons" onClick={this.handleClick}>{this.state.buttonText}</i>
                    </a>
                    <span>{this.state.response}</span>
                </div>
            </div>
        );
    }

    private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(this.state.buttonText === 'pause') {
            this.props.client.pause((res) => this.callBack(res, "play_arrow"))
        } else {
            this.props.client.resume((res) => this.callBack(res, "pause"))
        }

    };
}

export default PauseResumeComponent;