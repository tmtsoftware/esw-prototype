import * as React from 'react';
import './App.css'
import Client from "./sequencer/client/IFeederClient";
import FeederComponent from './sequencer/components/feeder/FeederComponent';

class App extends React.Component {

  public render() {
    const client = new Client();
    return (
      <div className="App">
        <FeederComponent client={client}/>
      </div>
    );
  }
}

export default App;
