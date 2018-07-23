import * as React from 'react';
import './App.css'
import EditorClient from "./sequencer/client/EditorClient";
import FeederClient from "./sequencer/client/IFeederClient";
import FeederComponent from './sequencer/components/feeder/FeederComponent';
import PauseComponent from "./sequencer/components/editor/PauseComponent";
import ResumeComponent from "./sequencer/components/editor/ResumeComponent";

class App extends React.Component {

    public render() {
        const feederClient = new FeederClient('http://localhost:8000');
        const editorClient = new EditorClient('http://localhost:8000');
        return (
            <div className="App">
                <FeederComponent client={feederClient}/>
                <PauseComponent client={editorClient}/>
                <ResumeComponent client={editorClient}/>
            </div>
        );
    }
}

export default App;
