import {AnnualCostsRequest, CarAnnualCosts} from "../proto/searchCar_pb";
import useSearchCarClient from "../hooks/useSearchCarClient";

import "../components/FormListLayout.scss"
import FormInputField from "../components/FormInputField";
import {useState} from "react";
import {notEmpty} from "../utils/ArrayUtils";
import {toCents} from "../utils/NumberUtils";
import {useAlert} from "../providers/AlertProvider";
import AnnualCostCarList from "../components/car/AnnualCostCarList";

export default function AnnualCosts() {

    const {showAlert} = useAlert()

    const searchCarClient = useSearchCarClient()
    const [fuelPrice, setFuelPrice] = useState('0')
    const [travelDistancePerMonth, setTravelDistancePerMonth] = useState('0')
    const [cars, setCars] = useState<CarAnnualCosts[]>()

    async function getAnnualCostsList() {
        const annualCostRequest = new AnnualCostsRequest()
        annualCostRequest.setFuelpriceincents(toCents(parseFloat(fuelPrice)))
        annualCostRequest.setTraveldistancepermonth(parseFloat(travelDistancePerMonth))

        await searchCarClient.rankCarsOnAnnualCosts(annualCostRequest, (error, responseMessage) => {
            if(error) {
                showAlert({title: 'Error', message: `Failed to retrieve cars ${error.message}`, type: 'ERROR'})
            } else {
                setCars(responseMessage?.getCarsList())
            }
        })
    }

    return (<div>
        <h1>Annual costs</h1>
        <div className="form-list-container">
            <form className="surface form-list-form" onSubmit={async (event) => {await getAnnualCostsList(); event.preventDefault()}}>
                <h2>Cost parameters</h2>
                <FormInputField label="Fuel price per litre" value={fuelPrice} onChange={setFuelPrice} type="number"/>
                <FormInputField label="Travel distance per month" value={travelDistancePerMonth} onChange={setTravelDistancePerMonth} type="number"/>

                <button type="submit">Search</button>
            </form>
        </div>
        <div className="form-list-list">
            {cars && <AnnualCostCarList cars={cars.map((car) => car).filter(notEmpty)}/>}
        </div>
    </div>)
}