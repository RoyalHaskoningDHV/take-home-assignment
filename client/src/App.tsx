import logo from './logo.svg';
import './App.scss';
import {SearchCarClient} from "./proto/searchCar_pb_service";
import {AnnualCostsRequest, Car, SearchRequest} from "./proto/searchCar_pb";
import {useState} from "react";
import CarList from "./components/CarList";

import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import CarCollection from "./screens/CarCollection";
import AddCar from "./screens/AddCar";
import AnnualCosts from "./screens/AnnualCosts";

const client = new SearchCarClient('http://localhost:8080')

function App() {

  const [cars, setCars] = useState<Car[] | undefined>(undefined)

  async function doStuff () {
    const searchRequest = new SearchRequest()
    searchRequest.setManufacturer("Opel")
    await client.search(searchRequest, (error, responseMessage) => {
      setCars(responseMessage?.getCarsList())
      // const carList = responseMessage?.getCarsList()
      // if(carList && carList.length > 0) {
      //   console.log('response cars!', carList[0].getManufacturer(), carList[0].getReleaseyear(), carList.length)
      // } else {
      //   console.log('No cars found')
      // }
      // if(error) {
      //   console.error('ERRR', error)
      // }
    })
  }

  return (
    <div className="App">
      {/*<header className="App-header">*/}

      {/*  <button onClick={doStuff}>*/}
      {/*    Search cars*/}
      {/*  </button>*/}

      {/*  <button onClick={addCar}>Add car</button>*/}

      {/*  <button onClick={getAnnualCostsList}>Get annual costs per car</button>*/}

      {/*  {cars && <CarList cars={cars}/>}*/}

      {/*</header>*/}
      <Router>
        <div>
          <nav>
            <ul>
              <li>
                <Link to="/">Catalogue</Link>
              </li>
              <li>
                <Link to="/add-car">Add car</Link>
              </li>
              <li>
                <Link to="/annual-costs">Annual costs</Link>
              </li>
            </ul>
          </nav>

          <Switch>
            <Route path="/annual-costs">
              <AnnualCosts />
            </Route>
            <Route path="/add-car">
              <AddCar />
            </Route>
            <Route path="/">
              <CarCollection />
            </Route>
          </Switch>
        </div>
      </Router>
    </div>
  );
}

export default App;
