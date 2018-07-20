import * as React from 'react';
import {Component} from 'react';
import styled from "styled-components";

const Button = styled.button`
        border-radius: 3px;
        padding: 0.25em 1em;
        margin: 0 1em;
        background: transparent;
        color: blue;
        border: 2px solid grey;
      `;

interface IProps {
    componentNameProp: string
    operation: string
    output: string,
    feedApi: (input:string) => void
}

interface IState {
    input: string
}

class IOOperationComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.updateInput = this.updateInput.bind(this);
        this.handleClick = this.handleClick.bind(this);

        this.state = this.getInitialState();
    }

    public render() {
        const {
            componentNameProp,
            operation,
            output,
        } = this.props;

        return (
            <div>
                <div>{componentNameProp} Request</div>
                <div><span><textarea className="Textarea" value={this.state.input} onChange={this.updateInput}/></span></div>
                <div>
                    <Button onClick={this.handleClick}>{operation}</Button>
                </div>
                <div><span><textarea className="Textarea" value={output}/></span></div>
            </div>
        );
    }

    private updateInput = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
        this.setState({
            input: event.target.value
        })
    };

    private handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        this.props.feedApi(this.state.input)
    };

    private getInitialState: () => IState = () => {
        return {input: ""}
    };
}

export default IOOperationComponent
