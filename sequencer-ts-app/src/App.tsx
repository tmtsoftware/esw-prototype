import * as React from 'react';
import EditorClient from "./sequencer/client/EditorClient";
import FeederClient from "./sequencer/client/FeederClient";
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
            const listClient = new ListComponentsClient("/locations");
            return <ListComponent client={listClient}/>
        };

        const Sequencer = (props: any) => {
            const sequencerPath = `${props.location.pathname}`;
            const feederClient = new FeederClient(sequencerPath);
            const editorClient = new EditorClient(sequencerPath);
            const resultClient = new ResultEventClient(sequencerPath);

            return <div className="row">
                <div className="col s6">
                    <FeederComponent client={feederClient} />
                    <PauseComponent client={editorClient} />
                    <ResumeComponent client={editorClient} />
                    <ShowSequenceComponent client={editorClient} />
                </div>
                <div className="col s6">
                    <ResultEventComponent client={resultClient}/>
                </div>
            </div>
        };

        const Assembly = (props: any) => {
            const assemblyPath = props.location.pathname;
            const assemblyClient = new AssemblyCommandWebClient(assemblyPath);

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
