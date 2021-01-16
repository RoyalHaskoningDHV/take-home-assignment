import logo from './logo.svg';
import './App.css';
import {SearchCarClient} from "./proto/searchCar_pb_service";
import {AnnualCostsRequest, Car, SearchRequest} from "./proto/searchCar_pb";
import {useState} from "react";
import CarList from "./components/CarList";

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

  async function getAnnualCostsList() {
    const annualCostRequest = new AnnualCostsRequest()
    annualCostRequest.setFuelpriceincents(150)
    annualCostRequest.setTraveldistancepermonth(1000)

    await client.rankCarsOnAnnualCosts(annualCostRequest, (error, responseMessage) => {
      responseMessage?.getCarsList().map((carData) => console.log('Cost for car per year', carData.getCar()?.getManufacturer(), carData.getCar()?.getModel(), carData.getAnnualcosts()))
    })
  }

  async function addCar() {
    const addCarRequest = new Car()
    addCarRequest.setManufacturer("Opel")
    addCarRequest.setModel("Astra")
    addCarRequest.setReleaseyear(2010)
    addCarRequest.setPriceincents(20000)
    addCarRequest.setFuelconsumption(13.4)
    addCarRequest.setMaintenancecostincents(40000)
    addCarRequest.setVersion("1.4 Turbo")
    client.addCar(addCarRequest, (error, responseMessage) => {
      if(!error) {
        console.log('successfully added')
      } else {
        console.error('Failed to add car :(')
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
          Search cars
        </button>

        <button onClick={addCar}>Add car</button>

        <button onClick={getAnnualCostsList}>Get annual costs per car</button>

        {cars && <CarList cars={cars}/>}

      </header>
    </div>
  );
}

export default App;
