package com.carcatalogue.server.repository

import com.carcatalogue.server.repository.model.CarData
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import org.litote.kmongo.*

interface CarRepo {
    fun addCar(car: CarData)
    fun getAllCars(manufacturer: String?, year: Int?): List<CarData>
}

class CarMongoRepo(databaseConnectionString: String, databaseName: String): CarRepo {

    private val mongoClient = KMongo.createClient(MongoClientSettings.builder().applyConnectionString(ConnectionString(databaseConnectionString)).build())
    private val database = mongoClient.getDatabase(databaseName)
    private val carCollection = database.getCollection<CarData>("cars")

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