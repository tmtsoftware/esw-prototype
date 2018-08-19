import * as React from 'react';
import HeaderComponent from "./sequencer/components/header/HeaderComponent";
import {HashRouter, Route} from "react-router-dom";
import {ListComponentPage} from "./sequencer/pages/ListComponentPage";
import {SequencerPage} from "./sequencer/pages/SequencerPage";
import {AssemblyPage} from "./sequencer/pages/AssemblyPage";

class App extends React.Component {
    public render() {
        return (
            <HashRouter>
                <div className="App">
                    <HeaderComponent/>
                    <Route exact={true} path="/" component={ListComponentPage}/>
                    <Route path="/sequencer" render={SequencerPage}/>
                    <Route path="/assembly" render={AssemblyPage}/>
                </div>
            </HashRouter>
        );
    }
}

export default App;
