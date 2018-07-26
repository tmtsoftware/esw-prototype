import * as React from 'react';
import {Component} from 'react';
import FeederClient from "../../client/IFeederClient";
import {IOOperationComponent} from '../IOOperationComponent';

interface IState {
    feedResponse: string
}

interface IProps {
    client: FeederClient
}


class FeederComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {feedResponse: ""}
    }

    public callBack = (res: string) => this.setState({
            feedResponse: res
        }
    );

    public render() {
        return (
            <IOOperationComponent
                componentNameProp="Sequence Feeder"
                operation="Feed"
                output={this.state.feedResponse}
                feedApi={this.feedApi}/>
        );
    }

    private feedApi = (input: string) => {
        this.props.client.feedApi(input, this.callBack)
    };
}

export default FeederComponent