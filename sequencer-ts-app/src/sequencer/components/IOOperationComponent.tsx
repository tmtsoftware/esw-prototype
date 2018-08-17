import * as React from 'react';
import {Component} from 'react';
import {Textarea, Button} from '../styles/styles';

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
            <div className="card-panel hoverable">
                <h6>{componentNameProp} Request</h6>
                <div>
                    <span>
                        <Textarea value={this.state.input} onChange={this.updateInput}/>
                    </span>
                </div>
                <div>
                    <Button primary={true} onClick={this.handleClick}>{operation}</Button>
                </div>
                <div>
                    <span>
                        <Textarea value={output}/>
                    </span>
                </div>
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

export {IOOperationComponent, Button}
