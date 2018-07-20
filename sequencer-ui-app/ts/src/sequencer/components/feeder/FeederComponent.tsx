import * as React from 'react';
import { Component } from 'react';
import * as request from "superagent";
import IOOperationComponent from '../IOOperationComponent';

interface IState {
    feedResponse: string
}


class FeederComponent extends Component<{}, IState> {

    constructor(props: {}) {
        super(props);
        this.state = {feedResponse: ""}
    }

    public render() {
        return (
            <IOOperationComponent componentNameProp="Sequence Feeder" operation="Feed" output={this.state.feedResponse} feedApi={this.feedApi}/>
        );
    }

    private feedApi = (input: string) => {
        request
            .post('http://localhost:8000/feeder/feed')
            .set('Content-Type', 'application/json')
            .send(input)
            .end((err, res) =>
                this.setState({
                    feedResponse: JSON.stringify(res.body,null,2)
                })
            );
    }
}

export default FeederComponent