import * as React from 'react';
import {Component} from "react";
import styled, {css, StyledFunction} from "styled-components";

interface IYourProps {
    primary: boolean
}

const styledButton: StyledFunction<IYourProps & React.HTMLProps<HTMLButtonElement>> = styled.button;

const CustomButton = styledButton`
        border-radius: 3px;
        padding: 0.25em 1em;
        margin: 0 1em;
        background: transparent;
        border: 2px solid teal;
        width: auto;
        
        ${props => props.primary && css`
        background: white;
        color: teal;`}
      `;

interface IProps {
    componentNameProp: string
    operation: string
    output: string,
    feedApi(input: string): void
}

interface IState {
    input: string
}

class IOOperationComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.updateInput = this.updateInput.bind(this);
        this.handleClick = this.handleClick.bind(this);

        this.state = {input: ""};
    }

    public render() {
        const {
            componentNameProp,
            operation,
            output,
        } = this.props;

        return (
            <div className="card-panel">
                <h6>{componentNameProp} Request</h6>
                <div><span><textarea className="text-area" value={this.state.input} onChange={this.updateInput}/></span>
                </div>
                <div>
                    <CustomButton primary={true} onClick={this.handleClick}>{operation}</CustomButton>
                </div>
                <div><span><textarea className="text-area" value={output}/></span></div>
            </div>
        );
    }

    private updateInput(event: React.ChangeEvent<HTMLTextAreaElement>) {
        this.setState({
            input: event.target.value
        })
    };

    private handleClick(event: React.MouseEvent<HTMLButtonElement>) {
        this.props.feedApi(this.state.input)
    };
}

export {IOOperationComponent, CustomButton}
