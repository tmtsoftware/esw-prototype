import * as React from 'react';
import {Component} from 'react';
import {ListComponentsClient} from "../client/ListComponentsClient";
import {Link} from "react-router-dom";
import styled from "styled-components";

const List = styled.ul`
   margin-top: 5%;
   margin-bottom: 5%;
   width: 70%;
`;

interface IProps {
    client: ListComponentsClient
}

interface IState {
    assemblies: string[]
    sequencers: string[]
}

class ListComponent extends Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {
            assemblies: [],
            sequencers: []
        };

        this.updateSequencerList = this.updateSequencerList.bind(this);
        this.updateAssemblyList = this.updateAssemblyList.bind(this);
    }

    public componentWillMount() {
        this.props.client.listSequencers(this.updateSequencerList);
        this.props.client.listAssemblies(this.updateAssemblyList);
    }

    public render() {
        return (
                <div>
                    <List className="collection with-header">
                        <li className="collection-header">Sequencers</li>
                        {
                            this.state.sequencers.map(
                                (value, index) =>
                                    <li key={index}><Link className="collection-item" to={value}>{value}</Link></li>
                            )
                        }
                    </List>
                    <List className="collection with-header">
                        <li className="collection-header">Assemblies</li>
                        {
                            this.state.assemblies.map(
                                (value, index) =>
                                    <li key={index}><Link className="collection-item" to={value}>{value}</Link></li>
                            )
                        }

                    </List>
                </div>
        )
    }

    private updateSequencerList(sequencers1: string) {
        this.setState({
            sequencers: JSON.parse(sequencers1)
        })
    };

    private updateAssemblyList(assemblies1: string) {
        this.setState({
            assemblies: JSON.parse(assemblies1)
        })
    };

}

export {ListComponent}
