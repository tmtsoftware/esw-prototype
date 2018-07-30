import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";
import {CustomButton} from "../IOOperationComponent";

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
                <div>Sequence Editor Pause</div>
                <div>
                    <CustomButton primary={true} onClick={this.handleClick}>Pause</CustomButton>
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