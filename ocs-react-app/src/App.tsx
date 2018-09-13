import * as React from 'react';
import HeaderComponent from "./ocs/components/header/HeaderComponent";
import {HashRouter, Route} from "react-router-dom";
import {ListComponentPage} from "./ocs/pages/ListComponentPage";
import {SequencerPage} from "./ocs/pages/SequencerPage";
import {AssemblyPage} from "./ocs/pages/AssemblyPage";

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
