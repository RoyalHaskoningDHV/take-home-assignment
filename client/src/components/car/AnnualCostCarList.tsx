import {CarAnnualCosts} from "../../proto/searchCar_pb";
import CarListItem from "./CarListItem";

import "./CarList.scss"

type Props = {
    cars: CarAnnualCosts[]
    numberOfYears: number
}

export default function AnnualCostCarList({cars, numberOfYears}: Props) {

    function getCosts(car: CarAnnualCosts): number {
        return (car.getAnnualcosts() * numberOfYears) / 100
    }

    return (
        <ol className="car-list">
            {cars.map((car) =>
                <CarListItem car={car.getCar()}>
                    <p>Annual costs: &euro; {getCosts(car)}</p>
                </CarListItem>)
            }
        </ol>
    )
}