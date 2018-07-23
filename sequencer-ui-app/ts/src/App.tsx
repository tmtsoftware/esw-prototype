import * as React from 'react';
import './App.css'
import Client from "./sequencer/client/IFeederClient";
import FeederComponent from './sequencer/components/feeder/FeederComponent';

class App extends React.Component {

  public render() {
    const client = new Client('http://localhost:8000');
    return (
      <div className="App">
        <FeederComponent client={client}/>
      </div>
    );
  }
}

export default App;
