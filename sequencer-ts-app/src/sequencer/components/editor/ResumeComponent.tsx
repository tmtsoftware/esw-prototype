import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";

interface IState {
    resumeResponse: string
}

interface IProps {
    client: EditorClient
}


class ResumeComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {resumeResponse: ""};
    }

    public callBack(res: string) {
        this.setState({resumeResponse: res});
    }

    public render() {
        return (
            <div>
                <h6>Sequence Editor Resume</h6>
                <div>
                    <a className="btn-large">
                        <i className="material-icons" onClick={this.handleClick}>play_arrow</i>
                    </a>
                    <span>{this.state.resumeResponse}</span>
                </div>
            </div>
        );
    }

    private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        this.props.client.resume((res) => this.callBack(res))
    };
}

export default ResumeComponent;