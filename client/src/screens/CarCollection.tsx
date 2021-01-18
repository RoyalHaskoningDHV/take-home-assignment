import {FormEvent, useCallback, useState} from "react";
import {Car} from "../proto/searchCar_pb";
import useSearchCarClient from "../hooks/useSearchCarClient";
import CarList from "../components/car/CarList";
import {useAlert} from "../providers/AlertProvider";
import FormInputField from "../components/FormInputField";

import "../components/FormListLayout.scss"
import {searchCars} from "../services/CarCollectionService";

export default function CarCollection() {
    const {showAlert} = useAlert()

    const searchCarClient = useSearchCarClient()
    const [manufacturer, setManufacturer] = useState('')
    const [year, setYear] = useState('')
    const [cars, setCars] = useState<Car[]>()
    const [loading, setLoading] = useState(false)

    const performSearch = useCallback(async () => {
        try {
            setLoading(true)
            const parsedYear = year ? parseInt(year, 10) : undefined
            const carResultList = await searchCars(searchCarClient, manufacturer, parsedYear)
            setCars(carResultList)
        } catch (e) {
            showAlert({type: 'ERROR', title: 'Failed to retrieve cars', message: e.message})
        } finally {
            setLoading(false)
        }
    }, [searchCarClient, year, manufacturer])

    async function handleSubmit(event: FormEvent) {
        event.preventDefault();
        await performSearch();
    }

    return (
        <>
            <h1>Car collection</h1>
            <div className="form-list-container">
            <form className="surface form-list-form" onSubmit={handleSubmit}>
                <h2>Filters</h2>
                <FormInputField label="Manufacturer" value={manufacturer} onChange={setManufacturer} />
                <FormInputField label="Release year" value={year} onChange={setYear} />

                <button type="submit">Search</button>
            </form>
            <div className="form-list-list">
                {loading && <p>Loading...</p>}
                {!loading && cars && <CarList cars={cars}/>}
                {!loading && !cars?.length && <p>No cars found</p>}
            </div>
            </div>
        </>)
}