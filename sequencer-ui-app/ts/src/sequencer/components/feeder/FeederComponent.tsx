import * as React from 'react';
import { Component } from 'react';
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
        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8000/feeder/feed', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onload = () => {
                this.setState({feedResponse: xhr.responseText})
        };
        xhr.send(input);
        alert("Button was clicked")
    }
}

export default FeederComponent