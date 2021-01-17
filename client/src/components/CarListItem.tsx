import {Car} from "../proto/searchCar_pb";

type Props = {
    car: Car
}

export default function CarListItem({car}: Props) {
    return (
        // TODO: Add a DB id as key here, so we can use react caching.
        <li key={car.getManufacturer()+car.getModel()+car.getReleaseyear()+Math.random()}>
            <h3>{car.getManufacturer()} {car.getModel()}</h3>
            <p>{car.getModel()} {car.getReleaseyear()}</p>
        </li>
    )
}