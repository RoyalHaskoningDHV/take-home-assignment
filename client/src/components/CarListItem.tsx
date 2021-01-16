import {Car} from "../proto/searchCar_pb";

type Props = {
    car: Car
}

export default function CarListItem({car}: Props) {
    return (
        <li key={car.getManufacturer()+car.getModel()+car.getReleaseyear()}>
            <h3>{car.getManufacturer()} {car.getModel()}</h3>
            <p>{car.getModel()} {car.getReleaseyear()}</p>
        </li>
    )
}