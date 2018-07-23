import * as React from 'react';
import {Component} from 'react';
import FeederClient from "../../client/IFeederClient";
import IOOperationComponent from '../IOOperationComponent';

interface IState {
    resumeResponse: string
}

interface IProps {
    client: FeederClient
}


class FeederComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {resumeResponse: ""}
    }

    public callBack = (res: string) => this.setState({
            resumeResponse: res
        }
    );

    public render() {
        return (
            <IOOperationComponent
                componentNameProp="Sequence Feeder"
                operation="Feed"
                output={this.state.resumeResponse}
                feedApi={this.feedApi}/>
        );
    }

    private feedApi = (input: string) => {
        this.props.client.feedApi(input, this.callBack)
    };
}

export default FeederComponent