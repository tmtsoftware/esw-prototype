import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";
import {CustomButton} from "../IOOperationComponent";

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
        this.handleClick = this.handleClick.bind(this)
    }

    public callBack(res: string) {
        this.setState({resumeResponse: res});
    }

    public render() {
        return (
            <div>
                <div>Sequence Editor Resume</div>
                <div>
                    <CustomButton primary={true} onClick={this.handleClick}>Resume</CustomButton>
                    <span>{this.state.resumeResponse}</span>
                </div>
            </div>
        );
    }

    private handleClick(event: React.MouseEvent<HTMLButtonElement>) {
        event.preventDefault();
        this.props.client.resume(this.callBack)
    };
}

export default ResumeComponent;