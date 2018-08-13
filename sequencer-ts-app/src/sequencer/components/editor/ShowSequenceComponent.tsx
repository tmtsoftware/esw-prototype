import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";
import {CustomButton} from "../IOOperationComponent";

interface IState {
    showSequenceResponse: string
}

interface IProps {
    client: EditorClient
    sequencerPath: string
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
            <div>
                <div>Sequence Editor Show Sequence</div>
                <div>
                    <CustomButton primary={true} onClick={this.handleClick}>Show Sequence</CustomButton>
                    <div><span><textarea className="text-area" value={this.state.showSequenceResponse}/></span></div>
                </div>
            </div>
        );
    }

    private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        this.props.client.showSequence(this.props.sequencerPath,(res) => this.callBack(res))
    };
}

export default ShowSequenceComponent;