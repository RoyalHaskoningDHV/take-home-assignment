import {Car} from "../proto/searchCar_pb";
import useSearchCarClient from "../hooks/useSearchCarClient";
import {useState} from "react";

import "../components/Surface.scss"
import FormInputField from "../components/FormInputField";
import {useAlert} from "../providers/AlertProvider";

export default function AddCar() {
    const {showAlert} = useAlert()

    const searchCarClient = useSearchCarClient()

    const [manufacturer, setManufacturer] = useState('')
    const [model, setModel] = useState('')
    const [releaseYear, setReleaseYear] = useState('')
    const [price, setPrice] = useState('')
    const [fuelConsumption, setFuelConsumption] = useState('')
    const [maintenanceCost, setMaintenanceCost] = useState('')
    const [version, setVersion] = useState('')

    async function addCar() {
        const addCarRequest = new Car()
        addCarRequest.setManufacturer(manufacturer)
        addCarRequest.setModel(model)
        addCarRequest.setReleaseyear(parseInt(releaseYear))
        addCarRequest.setPriceincents(parseFloat(price) * 100)
        addCarRequest.setFuelconsumption(parseFloat(fuelConsumption))
        addCarRequest.setMaintenancecostincents(parseFloat(maintenanceCost) * 100)
        addCarRequest.setVersion(version)
        searchCarClient.addCar(addCarRequest, (error, responseMessage) => {
            if(!error) {
                showAlert({title: 'Success', message: 'Car has been added', type: 'INFO'})
                clearForm()
            } else {
                showAlert({title: 'Error', message: `Failed to add car, ${error}`, type: 'ERROR'})
            }
        })
    }

    function clearForm() {
        [
            setManufacturer,
            setModel,
            setReleaseYear,
            setPrice,
            setFuelConsumption,
            setMaintenanceCost,
            setVersion
        ].forEach((fn) => fn(''))
    }

    return (<div>
        <h1>Add new car to the database</h1>
        <p>Fill out this form to add a new car</p>

        <form onSubmit={(event) => {addCar(); event.preventDefault()}} className="surface">

            <FormInputField label="Manufacturer" value={manufacturer} onChange={setManufacturer} required />
            <FormInputField label="Model" value={model} onChange={setModel} required />
            <FormInputField label="Version" value={version} onChange={setVersion} required />
            <FormInputField label="Release year" value={releaseYear} onChange={setReleaseYear} required type="number" />
            <FormInputField label="Price" value={price} onChange={setPrice} required type="number" />
            <FormInputField label="Fuel consumption (number of km/litre)" placeholder="13.5" value={fuelConsumption} onChange={setFuelConsumption} required type="number" />
            <FormInputField label="Maintenance cost per year" value={maintenanceCost} onChange={setMaintenanceCost} required type="number" />

            <button type="submit">Save car</button>
        </form>
    </div>)
}