import {CarAnnualCosts} from "../../proto/searchCar_pb";
import CarListItem from "./CarListItem";

import "./CarList.scss"

type Props = {
    cars: CarAnnualCosts[]
}

export default function AnnualCostCarList({cars}: Props) {
    return (
        <ol className="car-list">
            {cars.map((car) =>
                <CarListItem car={car.getCar()}>
                    <p>Annual costs: &euro; {car.getAnnualcosts() / 100}</p>
                </CarListItem>)
            }
        </ol>
    )
}