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

class App extends React.Component {

    public render() {
        const feederClient = new FeederClient('http://localhost:9000/sequencer/iris/darknight');
        const editorClient = new EditorClient('http://localhost:9000/sequencer/iris/darknight');
        const resultClient = new ResultEventClient('http://localhost:9000/sequencer/iris/darknight');
        return (
            <div className="App">
                <HeaderComponent/>
                <FeederComponent client={feederClient}/>
                <PauseComponent client={editorClient}/>
                <ResumeComponent client={editorClient}/>
                <ShowSequenceComponent client={editorClient}/>
                <ResultEventComponent client={resultClient}/>
                </div>
        );
    }
}

export default App;
