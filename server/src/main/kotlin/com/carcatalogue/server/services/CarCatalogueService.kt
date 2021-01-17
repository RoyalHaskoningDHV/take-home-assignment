package com.carcatalogue.server.services

import com.carcatalogue.proto.*
import com.carcatalogue.server.repository.CarRepo
import com.carcatalogue.server.repository.model.CarData
import com.carcatalogue.server.services.recommendations.AnnualCostsRecommendation
import com.google.protobuf.Empty

/**
 * Service for interacting with the car catalogue.
 */
class CarCatalogueService(private val carRepo: CarRepo) : SearchCarGrpcKt.SearchCarCoroutineImplBase() {

    override suspend fun search(request: SearchRequest): SearchReply {
        // SearchRequest is defaulting the releaseYear to 0 if none is given,
        // SearchRequest is defaulting the manufacturer to "" if none is given,
        // in a production environment it would be advised to research how nullability can be done correctly.
        val releaseYear = if(request.releaseYear != 0) request.releaseYear else null
        val manufacturer = if(request.manufacturer.isNotEmpty()) request.manufacturer else null

        val cars = carRepo.getAllCars(manufacturer, releaseYear)
        return SearchReply
            .newBuilder()
            .addAllCars(cars.map { it.toResponseModel() })
            .build()
    }

    override suspend fun addCar(request: Car): Empty {
        carRepo.addCar(request.toCarData())
        return Empty.getDefaultInstance()
    }

    override suspend fun rankCarsOnAnnualCosts(request: AnnualCostsRequest): AnnualCostsReply {
        val carData = AnnualCostsRecommendation()
            .sortByAnnualCosts(carRepo.getAllCars(), request.fuelPriceInCents, request.travelDistancePerMonth)
            .map {
                CarAnnualCosts
                    .newBuilder()
                    .setCar(it.car.toResponseModel())
                    .setAnnualCosts(it.annualCosts)
                    .build()
            }

        return AnnualCostsReply.newBuilder().addAllCars(carData).build()
    }
}

fun CarData.toResponseModel(): Car {
    return Car.newBuilder()
        .setManufacturer(manufacturer)
        .setModel(model)
        .setPriceInCents(priceInCents)
        .setVersion(version)
        .setReleaseYear(releaseYear)
        .setFuelConsumption(fuelConsumption)
        .setMaintenanceCostInCents(maintenanceCostInCents)
        .build()
}

fun Car.toCarData(): CarData {
    return CarData(
        model = model,
        manufacturer = manufacturer,
        version = version,
        releaseYear = releaseYear,
        priceInCents = priceInCents,
        fuelConsumption = fuelConsumption,
        maintenanceCostInCents = maintenanceCostInCents,
    )
}