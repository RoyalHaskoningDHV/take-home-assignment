import {Car} from "../proto/searchCar_pb";
import useSearchCarClient from "../hooks/useSearchCarClient";

export default function AddCar() {

    const searchCarClient = useSearchCarClient()

    async function addCar() {
        const addCarRequest = new Car()
        addCarRequest.setManufacturer("Opel")
        addCarRequest.setModel("Astra")
        addCarRequest.setReleaseyear(2010)
        addCarRequest.setPriceincents(20000)
        addCarRequest.setFuelconsumption(13.4)
        addCarRequest.setMaintenancecostincents(40000)
        addCarRequest.setVersion("1.4 Turbo")
        searchCarClient.addCar(addCarRequest, (error, responseMessage) => {
            if(!error) {
                console.log('successfully added')
            } else {
                console.error('Failed to add car :(')
            }
        })
    }

    return (<div><h1>Add car</h1></div>)
}