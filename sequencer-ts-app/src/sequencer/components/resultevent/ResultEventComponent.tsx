import * as React from 'react';
import {Component} from 'react';
import ResultEventClient from "../../client/ResultEventClient";
import styled from "styled-components";

const ResultDiv = styled.div`
    left: 50%;
    bottom: 1703px;
    padding: 20px;
    font-family: Verdana;
    font-size: 16px;
    color: rgb(30, 30, 30);
    overflow-x: auto;
`;

const ResultList = styled.ul`
   box-sizing: border-box;
   background-color: rgb(255, 255, 255);
   color: rgb(30, 30, 30);
   box-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
   margin: 10px 0px 0px 10px;
   padding: 10px;
   font-family: monospace;
   overflow: auto;
   position: fixed;
   top: 130px;
   width: 100%;
   height: 430px;
`;

const ResultTitle = styled.h6`
   position: fixed;
`;

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
                <ResultTitle>Sequencer Results Stream</ResultTitle>
                <ResultDiv>
                    <ResultList>
                        {this.state.results.map((value: string, index: number) => <li key={index}>{value}</li>)}
                    </ResultList>
                </ResultDiv>
            </div>
        );
    }
}

export default ResultEventComponent;
