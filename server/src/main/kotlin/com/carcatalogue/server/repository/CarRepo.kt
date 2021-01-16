package com.carcatalogue.server.repository

import com.carcatalogue.server.repository.model.CarData
import com.mongodb.client.MongoCollection
import org.litote.kmongo.*

/**
 * Provides access to the storage of cars.
 * Note, right now this uses the synchronous driver, better thread management should be considered when creating an actual application.
 */
interface CarRepo {
    /**
     * Adds a new car to the database.
     */
    fun addCar(car: CarData)

    /**
     * Returns the available cars.
     * @param manufacturer, the manufacturer of the car, if empty, all manufacturers will be included.
     * @param year, the production year of the car, if empty, all years will be included.
     */
    fun getAllCars(manufacturer: String? = null, year: Int? = null): List<CarData>
}

class CarMongoRepo(private val carCollection: MongoCollection<CarData>): CarRepo {

    override fun addCar(car: CarData) {
        carCollection.insertOne(car)
    }

    override fun getAllCars(manufacturer: String?, year: Int?): List<CarData> {
        // When the database gets big, we would want to use a paging mechanism.
        // The solution below is not scalable.
        val yearFilter = if(year !== null) CarData::releaseYear eq year else null
        val manufacturerFilter = if(manufacturer !== null) CarData::manufacturer eq manufacturer else null
        return carCollection.find(manufacturerFilter, yearFilter).toList()
    }
}