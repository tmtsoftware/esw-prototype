import AssemblyCommandWebClient from "../../client/AssemblyCommandWebClient";
import {Component} from "react";
import {IOOperationComponent} from "../IOOperationComponent";
import * as React from "react";


interface IState {
    submitResponse: string
}

interface IProps {
    client: AssemblyCommandWebClient
    assemblyPath: string
}

class AssemblyCommandComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {submitResponse: ""}
    }

    public callBack(res: string) {
        this.setState({submitResponse: res});
    }

    public render() {
        return (
            <IOOperationComponent
                componentNameProp="Assembly Command Feeder"
                operation="Submit"
                output={this.state.submitResponse}
                feedApi={this.submit}/>
        );
    }

    private submit = (input: string) => {
        this.props.client.submit(this.props.assemblyPath, input, (res) => this.callBack(res))
    };

}

export default AssemblyCommandComponent