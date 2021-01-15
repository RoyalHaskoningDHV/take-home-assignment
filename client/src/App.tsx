import logo from './logo.svg';
import './App.css';
import {Dog, DogClient} from "./proto/dog_pb_service";
import {BarkRequest} from "./proto/dog_pb";

function App() {

  async function doStuff () {
    const client = new DogClient('http://localhost:8080')
    const request = new BarkRequest()
    await client.bark(request, (error, responseMessage) => {
      console.log('response!', responseMessage?.getMessage())
      if(error) {
        console.error('ERRR', error)
      }
    })
  }

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <button onClick={doStuff}>
          Learn React
        </button>
      </header>
    </div>
  );
}

export default App;
