import * as React from 'react';
import {Component} from 'react';
import ResultEventClient from "../../client/ResultEventClient";

interface IState {
    results: string[]
}

interface IProps {
    client: ResultEventClient
}


class ResultEventComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {results: []};

        this.callBack = this.callBack.bind(this)
    }

    public componentWillMount() {
        if (!(this.state.results.length)) {
            this.props.client.onMessage(this.callBack)
        }
    }

    public componentWillUnmount() {
        this.props.client.close()
    }

    public callBack(evt: MessageEvent) {
        if (evt.data.toString()) {
            this.setState({
                    results: this.state.results.concat(evt.data.toString())
                        .concat("\n".concat("*".repeat(50)).concat("\n"))
                }
            )
        }
    };

    public render() {
        return (
            <div>
                <h6 className="right-title">Sequencer Results Stream</h6>
                <div className="right-column">
                    {console.log(`${this.state.results.length}`)}
                    <ul className="result-text-area">
                        {this.state.results.map((value: string, index: number) => <li key={index}>{value}</li>)}
                    </ul>
                </div>
            </div>
        );
    }
}

export default ResultEventComponent;
