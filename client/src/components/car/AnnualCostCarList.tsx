import {CarAnnualCosts} from "../../proto/searchCar_pb";
import CarListItem from "./CarListItem";

import "./CarList.scss"

type Props = {
    cars: CarAnnualCosts[]
    numberOfYears: number
}

export default function AnnualCostCarList({cars, numberOfYears}: Props) {

    function getCosts(car: CarAnnualCosts, numberOfYears: number): number {
        return (car.getAnnualcosts() * numberOfYears) / 100
    }

    function renderCarWithCosts(carWithCosts: CarAnnualCosts) {
        // TODO: gRPC's nullability isn't great with typescript, in a real application, we need to figure out how to deal with this.
        const car = carWithCosts.getCar()
        if(car) {
            return (
                // TODO: Key should be some unique id per car
                <CarListItem key={Math.random()} car={car}>
                    <p>Annual costs: &euro; {getCosts(carWithCosts, 1)}</p>
                    <p>Costs of {numberOfYears} years: &euro; {getCosts(carWithCosts, numberOfYears)}</p>
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