package com.carcatalogue.server.repository

import com.carcatalogue.server.repository.model.CarData
import com.mongodb.client.MongoCollection
import org.litote.kmongo.*

interface CarRepo {
    fun addCar(car: CarData)
    fun getAllCars(manufacturer: String?, year: Int?): List<CarData>
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