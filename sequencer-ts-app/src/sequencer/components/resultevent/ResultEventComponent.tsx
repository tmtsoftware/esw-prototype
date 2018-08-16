import * as React from 'react';
import {Component} from 'react';
import ResultEventClient from "../../client/ResultEventClient";
import jss, {Styles} from 'jss';

interface IState {
    results: string[]
}

interface IProps {
    client: ResultEventClient
}

const styles: Partial<Styles<any>> = {
        resultTextarea: {
            'background-color': 'rgb(255, 255, 255)',
            'box-shadow': '0 1px 2px rgba(0, 0, 0, 0.3)',
            'box-sizing': 'border-box',
            color: 'rgb(30, 30, 30)',
            'font-family': 'monospace',
            height: '430px',
            margin: '10px 0px 0px 10px',
            overflow: 'auto',
            padding: '10px',
            position: 'fixed',
            top: '130px',
            width: '100%'
        },
        rightColumn: {
            bottom: '1703px',
            color: 'rgb(30, 30, 30)',
            'font-family': 'Verdana',
            'font-size': '16px',
            left: '50%',
            'overflow-x': 'auto',
            padding: '20px'
        },
        rightTitle: {
            position: 'fixed'
        }
    };

const {classes} = jss.createStyleSheet(styles).attach();

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
                <h6 className={classes.rightTitle}>Sequencer Results Stream</h6>
                <div className={classes.rightColumn}>
                    <ul className={classes.resultTextarea}>
                        {this.state.results.map((value: string, index: number) => <li key={index}>{value}</li>)}
                    </ul>
                </div>
            </div>
        );
    }
}

export default ResultEventComponent;
