import * as React from 'react';
import './App.css'
import EditorClient from "./sequencer/client/EditorClient";
import FeederClient from "./sequencer/client/IFeederClient";
import FeederComponent from './sequencer/components/feeder/FeederComponent';
import PauseComponent from "./sequencer/components/editor/PauseComponent";
import ResumeComponent from "./sequencer/components/editor/ResumeComponent";
import ShowSequenceComponent from "./sequencer/components/editor/ShowSequenceComponent";
import ResultEventComponent from "./sequencer/components/resultevent/ResultEventComponent";
import ResultEventClient from "./sequencer/client/ResultEventClient";
import HeaderComponent from "./sequencer/components/header/HeaderComponent";
import {ListComponent} from "./sequencer/components/ListComponent";
import {ListComponentsClient} from "./sequencer/client/ListComponentsClient";
import {HashRouter, Route, /*Link*/} from "react-router-dom";
import AssemblyCommandWebClient from "./sequencer/client/AssemblyCommandWebClient";
import AssemblyCommandComponent from "./sequencer/components/assembly/AssemblyCommandComponent";

class App extends React.Component {

    public render() {
        const feederClient = new FeederClient('http://localhost:9090');
        const editorClient = new EditorClient('http://localhost:9090');
        const listClient = new ListComponentsClient('http://localhost:9090');
        const assemblyClient = new AssemblyCommandWebClient('http://localhost:9090');

        const List = () => {
            return <ListComponent client={listClient}/>
        };

        const Sequencer = (props: any) => {
            const resultClient = new ResultEventClient('http://localhost:9090', props.location.pathname);

            return <div>
                <FeederComponent client={feederClient} sequencerPath={props.location.pathname}/>
                <PauseComponent client={editorClient} sequencerPath={props.location.pathname}/>
                <ResumeComponent client={editorClient} sequencerPath={props.location.pathname}/>
                <ShowSequenceComponent client={editorClient} sequencerPath={props.location.pathname}/>
                <ResultEventComponent client={resultClient}/>
            </div>
        };

        const Assembly = (props: any) => {
            return <div>
                <AssemblyCommandComponent client={assemblyClient} assemblyPath={props.location.pathname}/>
            </div>
        };

        return (
            <HashRouter>
                <div className="App">
                    <HeaderComponent/>
                    <Route exact={true} path="/" component={List}/>
                    <Route path="/sequencer" render={Sequencer}/>
                    <Route path="/assembly" render={Assembly}/>
                </div>
            </HashRouter>
        );
    }
}

export default App;
