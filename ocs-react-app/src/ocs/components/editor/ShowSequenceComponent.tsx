import * as React from 'react';
import {Component} from 'react';
import EditorClient from "../../client/EditorClient";
import {Button} from "../IOOperationComponent";
import {Textarea} from '../../styles/styles';

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
                    <Button primary={true} onClick={this.handleClick}>Show Sequence</Button>
                    <div><span><Textarea value={this.state.showSequenceResponse}/></span></div>
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