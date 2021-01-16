package com.carcatalogue.server.services.recommendations

import com.carcatalogue.server.repository.model.CarData

private const val MONTHS_PER_YEAR = 12

class AnnualCostsRecommendation {
    fun sortByAnnualCosts(cars: List<CarData>, fuelPriceInCents: Int, travelDistancePerMonth: Int): List<CarDataAnnualCosts> {
        return cars
            .map { CarDataAnnualCosts(car = it, getAnnualCostsInCents(it, fuelPriceInCents, travelDistancePerMonth)) }
            .sortedBy { it.annualCosts }
    }

    fun getAnnualCostsInCents(car: CarData, fuelPriceInCents: Int, travelDistancePerMonth: Int): Long {
        val fuelLitres = (travelDistancePerMonth * MONTHS_PER_YEAR) / car.fuelConsumption
        return Math.round(fuelLitres * fuelPriceInCents + car.maintenanceCostInCents)
    }
}

data class CarDataAnnualCosts(val car: CarData, val annualCosts: Long)