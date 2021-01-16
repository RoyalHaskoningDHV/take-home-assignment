import {AnnualCostsRequest} from "../proto/searchCar_pb";
import useSearchCarClient from "../hooks/useSearchCarClient";

export default function AnnualCosts() {

    const searchCarClient = useSearchCarClient()

    async function getAnnualCostsList() {
        const annualCostRequest = new AnnualCostsRequest()
        annualCostRequest.setFuelpriceincents(150)
        annualCostRequest.setTraveldistancepermonth(1000)

        await searchCarClient.rankCarsOnAnnualCosts(annualCostRequest, (error, responseMessage) => {
            responseMessage?.getCarsList().map((carData) => console.log('Cost for car per year', carData.getCar()?.getManufacturer(), carData.getCar()?.getModel(), carData.getAnnualcosts()))
        })
    }

    return (<div><h1>Annual costs</h1></div>)
}