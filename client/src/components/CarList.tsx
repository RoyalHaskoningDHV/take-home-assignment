import {Car} from "../proto/searchCar_pb";
import CarListItem from "./CarListItem";

type Props = {
    cars: Car[]
}

export default function CarList({cars}: Props) {
    return (
        <ol>
            {cars.map((car) => CarListItem({car}))}
        </ol>
    )
}