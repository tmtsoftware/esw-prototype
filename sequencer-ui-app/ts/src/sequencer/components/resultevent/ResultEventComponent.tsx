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

    public componentWillMount(){
        if (!(this.state.results && this.state.results.length)) {
            this.props.client.onMessage(this.callBack)
        }
    }

    public componentWillUnmount(){
        this.props.client.close()
    }

    public callBack = (evt: MessageEvent) => this.setState({
            results: this.state.results.concat(evt.data.toString())
        }
    );

    public render() {
        return (
            <div>
                <p className="ResultTitleAreaCss">Sequencer Results Stream</p>
                <div className="RightColumnCss">
                    {console.log(`${this.state.results.length}`)}
                    <ul className="ResultTextAreaCss">
                        {this.state.results.map((value: string, index: number) => <li key={index}>{value}</li>)}
                    </ul>
                </div>
            </div>
        );
    }
}

export default ResultEventComponent;
