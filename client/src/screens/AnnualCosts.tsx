import {CarAnnualCosts} from "../proto/searchCar_pb";
import useSearchCarClient from "../hooks/useSearchCarClient";

import "../components/FormListLayout.scss"
import FormInputField from "../components/FormInputField";
import {FormEvent, useCallback, useState} from "react";
import {notEmpty} from "../utils/ArrayUtils";
import {toCents} from "../utils/NumberUtils";
import {useAlert} from "../providers/AlertProvider";
import AnnualCostCarList from "../components/car/AnnualCostCarList";
import {searchAnnualCostRecommendation} from "../services/CarCollectionService";

const SHOW_COSTS_NUMBER_OF_YEARS = 4;

export default function AnnualCosts() {

    const {showAlert} = useAlert()

    const searchCarClient = useSearchCarClient()
    const [fuelPrice, setFuelPrice] = useState('0')
    const [travelDistancePerMonth, setTravelDistancePerMonth] = useState('0')
    const [cars, setCars] = useState<CarAnnualCosts[]>()
    const [loading, setLoading] = useState(false)

    const searchAnnualCostsList = useCallback(async () => {
        try {
            setLoading(true)
            const fuelPriceInCents = toCents(parseFloat(fuelPrice))
            const parsedTravelDistance = parseFloat(travelDistancePerMonth)
            const carResultList = await searchAnnualCostRecommendation(searchCarClient, fuelPriceInCents, parsedTravelDistance)
            setCars(carResultList)
        } catch (e) {
            showAlert({type: 'ERROR', title: 'Failed to retrieve cars', message: e.message})
        } finally {
            setLoading(false)
        }
    }, [searchCarClient, showAlert, fuelPrice, travelDistancePerMonth])

    async function handleSubmit(event: FormEvent) {
        event.preventDefault();
        await searchAnnualCostsList();
    }

    return (
        <>
            <h1>Annual costs</h1>
            <div className="form-list-container">
                <form className="surface form-list-form" onSubmit={handleSubmit}>
                    <h2>Cost parameters</h2>
                    <FormInputField label="Fuel price per litre" value={fuelPrice} onChange={setFuelPrice} type="number"/>
                    <FormInputField label="Travel distance per month" value={travelDistancePerMonth} onChange={setTravelDistancePerMonth} type="number"/>

                    <button type="submit">Search</button>
                </form>
                <div className="form-list-list">
                    {loading && <p>Loading...</p>}

                    {!loading && cars && <AnnualCostCarList
                        numberOfYears={SHOW_COSTS_NUMBER_OF_YEARS}
                        cars={cars.map((car) => car).filter(notEmpty)}/>}

                    {!loading && !cars?.length && <p>No cars found</p>}
                </div>
            </div>
        </>
    )
}