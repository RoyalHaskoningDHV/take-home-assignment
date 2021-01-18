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

    function renderCarWithCosts(carWithCosts: CarAnnualCosts) {
        // TODO: gRPC's nullability isn't great with typescript, in a real application, we need to figure out how to deal with this.
        const car = carWithCosts.getCar()
        if(car) {
            return (
                <CarListItem car={car}>
                    <p>Annual costs: &euro; {getCosts(carWithCosts)}</p>
                </CarListItem>
            )
        }
    }

    return (
        <ol className="car-list">
            {cars.map(renderCarWithCosts)}
        </ol>
    )
}