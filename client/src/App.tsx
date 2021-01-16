import logo from './logo.svg';
import './App.css';
import {SearchCarClient} from "./proto/searchCar_pb_service";
import {SearchRequest} from "./proto/searchCar_pb";

function App() {

  async function doStuff () {
    const client = new SearchCarClient('http://localhost:8080')

    const searchRequest = new SearchRequest()
    searchRequest.setManufacturer("Citroen")
    await client.search(searchRequest, (error, responseMessage) => {
      console.log('response cars!', responseMessage?.getCarsList()[0].getManufacturer(), responseMessage?.getCarsList()[0].getProductionyear())
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
