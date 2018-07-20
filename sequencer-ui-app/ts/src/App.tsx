import * as React from 'react';
import './App.css'
import FeederComponent from './sequencer/components/feeder/FeederComponent';

class App extends React.Component {

  public render() {
    return (
      <div className="App">
        <FeederComponent/>
      </div>
    );
  }
}

export default App;
