import {Car} from "../../proto/searchCar_pb";
import CarListItem from "./CarListItem";

import "./CarList.scss"

type Props = {
    cars: Car[]
}

export default function CarList({cars}: Props) {
    return (
        <ol className="car-list">
            {cars.map((car) => CarListItem({car}))}
        </ol>
    )
}