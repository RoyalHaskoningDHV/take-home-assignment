import {Car} from "../../proto/searchCar_pb";

import "./CarListItem.scss"

type Props = {
    car: Car
}

export default function CarListItem({car}: Props) {
    return (
        // TODO: Add a DB id as key here, so we can use react caching.
        <li className="car-list-item" key={car.getManufacturer()+car.getModel()+car.getReleaseyear()+Math.random()}>
            <h3 className="car-list-item-heading">{car.getManufacturer()} {car.getModel()}</h3>
            <p className="car-list-item-sub-heading">{car.getVersion()}</p>
            <ul className="car-list-item-specs">
                <li>Year: {car.getReleaseyear()}</li>
                <li>Price: {car.getPriceincents() / 100}</li>
                <li>Fuel consumption km/l: {car.getFuelconsumption()}</li>
                <li>Maintenance per year: {car.getMaintenancecostincents() / 100}</li>
            </ul>
        </li>
    )
}