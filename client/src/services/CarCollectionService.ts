import {AnnualCostsRequest, Car, CarAnnualCosts, SearchRequest} from "../proto/searchCar_pb";
import {SearchCarClient} from "../proto/searchCar_pb_service";

/**
 * Requests the api to return all cars matching the given parameters.
 */
export async function searchCars(client: SearchCarClient, manufacturer?: string, year?: number): Promise<Car[] | undefined> {
    const searchRequest = new SearchRequest()
    !!manufacturer && searchRequest.setManufacturer(manufacturer)
    !!year && searchRequest.setReleaseyear(year)

    return await new Promise((resolve, reject) => {
        client.search(searchRequest, (error, responseMessage) => {
            console.log('error?', error)
            if(error) {
                return reject(error)
            }
            resolve(responseMessage?.getCarsList())
        })
    });
}

/**
 * Requests the api to return all cars with their annual costs based on the given parameters.
 */
export async function searchAnnualCostRecommendation(client: SearchCarClient, fuelPriceInCents: number, travelDistancePerMonth: number): Promise<CarAnnualCosts[] | undefined> {
    const annualCostRequest = new AnnualCostsRequest()
    annualCostRequest.setFuelpriceincents(fuelPriceInCents)
    annualCostRequest.setTraveldistancepermonth(travelDistancePerMonth)

    return await new Promise(((resolve, reject) => {
        client.rankCarsOnAnnualCosts(annualCostRequest, (error, responseMessage) => {
            if(error) {
                return reject(error)
            }
            resolve(responseMessage?.getCarsList())
        })
    }))
}

/**
 * Adds a new car to the database.
 */
export async function saveCarSpecifications(client: SearchCarClient, spec: Car): Promise<void> {
    return await new Promise(((resolve, reject) => {
        client.addCar(spec, (error, responseMessage) => {
            if(error) {
                return reject(error)
            }
            resolve(responseMessage)
        })
    }))
}