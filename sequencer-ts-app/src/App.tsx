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
import {HashRouter, Route} from "react-router-dom";
import AssemblyCommandWebClient from "./sequencer/client/AssemblyCommandWebClient";
import AssemblyCommandComponent from "./sequencer/components/assembly/AssemblyCommandComponent";

class App extends React.Component {

    public render() {
        const List = () => {
            const listClient = new ListComponentsClient('http://localhost:9090');
            return <ListComponent client={listClient}/>
        };

        const Sequencer = (props: any) => {
            const sequencerPath = `${props.location.pathname}`;
            const feederClient = new FeederClient(`http://localhost:9090${sequencerPath}`);
            const editorClient = new EditorClient(`http://localhost:9090${sequencerPath}`);
            const resultClient = new ResultEventClient(`http://localhost:9090${sequencerPath}`);

            return <div>
                <FeederComponent client={feederClient} />
                <PauseComponent client={editorClient} />
                <ResumeComponent client={editorClient} />
                <ShowSequenceComponent client={editorClient} />
                <ResultEventComponent client={resultClient}/>
            </div>
        };

        const Assembly = (props: any) => {
            const assemblyPath = props.location.pathname;
            const assemblyClient = new AssemblyCommandWebClient(`http://localhost:9090${assemblyPath}`);

            return <div>
                <AssemblyCommandComponent client={assemblyClient}/>
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
