import {useCallback, useState} from "react";
import {Car, SearchRequest} from "../proto/searchCar_pb";
import useSearchCarClient from "../hooks/useSearchCarClient";
import CarList from "../components/CarList";

export default function CarCollection() {
    const searchCarClient = useSearchCarClient()
    const [manufacturer, setManufacturer] = useState<string>()
    const [year, setYear] = useState<string>()
    const [cars, setCars] = useState<Car[]>()

    function searchCars() {
        const searchRequest = new SearchRequest()
        manufacturer && searchRequest.setManufacturer(manufacturer)
        year && searchRequest.setManufacturer(year)
        searchCarClient.search(searchRequest, (error, responseMessage) => {
            setCars(responseMessage?.getCarsList())
            if(error) {
                // TODO: Show error.
            }
        })
    }


    return (<div>
        {cars && <CarList cars={cars}/>}
    </div>)
}