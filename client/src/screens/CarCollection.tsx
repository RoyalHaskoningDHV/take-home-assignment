import {useState} from "react";
import {Car, SearchRequest} from "../proto/searchCar_pb";
import useSearchCarClient from "../hooks/useSearchCarClient";
import CarList from "../components/car/CarList";
import {useAlert} from "../providers/AlertProvider";
import FormInputField from "../components/FormInputField";

import "../components/FormListLayout.scss"

export default function CarCollection() {
    const {showAlert} = useAlert()

    const searchCarClient = useSearchCarClient()
    const [manufacturer, setManufacturer] = useState('')
    const [year, setYear] = useState('')
    const [cars, setCars] = useState<Car[]>()
    const [loading, setLoading] = useState(false)

    function searchCars() {
        const searchRequest = new SearchRequest()
        !!manufacturer && searchRequest.setManufacturer(manufacturer)
        !!year && searchRequest.setReleaseyear(parseInt(year, 10))
        setLoading(true)

        searchCarClient.search(searchRequest, (error, responseMessage) => {
            setLoading(false)
            setCars(responseMessage?.getCarsList())
            if(error) {
                showAlert({type: 'ERROR', title: 'Failed to retrieve cars', message: error.message})
            }
        })
    }


    return (<div className="form-list-container">
        <form className="surface form-list-form" onSubmit={(event) => {searchCars(); event.preventDefault()}}>
            <h2>Filter cars</h2>
            <FormInputField label="Manufacturer" value={manufacturer} onChange={setManufacturer} />
            <FormInputField label="Release year" value={year} onChange={setYear} />

            <button type="submit">Search</button>
        </form>
        <div className="form-list-list">
            {loading && <div>Loading...</div>}
            {!loading && cars && <CarList cars={cars}/>}
        </div>
    </div>)
}