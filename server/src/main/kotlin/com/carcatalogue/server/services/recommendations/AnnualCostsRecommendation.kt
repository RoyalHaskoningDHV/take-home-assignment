package com.carcatalogue.server.services.recommendations

import com.carcatalogue.server.repository.model.CarData

private const val MONTHS_PER_YEAR = 12
private const val DEPRECIATION_PERCENTAGE_PER_YEAR = 5

class AnnualCostsRecommendation {
    /**
     * Sorts the given list of cars based on annual cost.
     * @param fuelPriceInCents, the amount a litre of fuel costs (in cents).
     * @param travelDistancePerMonth, the distance that is expected to be travelled with the car (in km).
     * @return list of cars, including annual costs (in cents), sorted on annual costs.
     */
    fun sortByAnnualCosts(cars: List<CarData>, fuelPriceInCents: Int, travelDistancePerMonth: Int): List<CarDataAnnualCosts> {
        return cars
            .map { CarDataAnnualCosts(car = it, getAnnualCostsInCents(it, fuelPriceInCents, travelDistancePerMonth)) }
            .sortedBy { it.annualCosts }
    }

    /**
     * Calculates the annual costs of the car.
     * @param fuelPriceInCents, the amount a litre of fuel costs (in cents).
     * @param travelDistancePerMonth, the distance that is expected to be travelled with the car (in km).
     * @return the annual costs of the given car, in cents.
     */
    fun getAnnualCostsInCents(car: CarData, fuelPriceInCents: Int, travelDistancePerMonth: Int): Long {
        val fuelLitres = (travelDistancePerMonth * MONTHS_PER_YEAR) / car.fuelConsumption
        // TODO: Check with the product owner on how to calculate depreciation
        val depreciationPerYear = car.priceInCents * (DEPRECIATION_PERCENTAGE_PER_YEAR / 100f)
        return Math.round(fuelLitres * fuelPriceInCents + car.maintenanceCostInCents + depreciationPerYear)
    }
}

data class CarDataAnnualCosts(val car: CarData, val annualCosts: Long)