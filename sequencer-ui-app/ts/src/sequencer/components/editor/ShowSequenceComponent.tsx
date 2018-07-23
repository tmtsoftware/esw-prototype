import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";
import {CustomButton} from "../IOOperationComponent";

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
        this.handleClick = this.handleClick.bind(this)
    }

    public callBack = (res: string) => this.setState({showSequenceResponse: res}
    );

    public render() {
        return (
            <div>
                <div>Sequence Editor Show Sequence</div>
                <div>
                    <CustomButton primary={true} onClick={this.handleClick}>Show Sequence</CustomButton>
                    <div><span><textarea className="Textarea" value={this.state.showSequenceResponse}/></span></div>
                </div>
            </div>
        );
    }

    private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        this.props.client.showSequence(this.callBack)
    };
}

export default ShowSequenceComponent;