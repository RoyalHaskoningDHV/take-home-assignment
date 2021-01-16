package com.carcatalogue.server.repository

import com.carcatalogue.server.CAR_REPO_URL
import com.carcatalogue.server.mockdata.Cars
import com.carcatalogue.server.repository.model.CarData
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import de.bwaldvogel.mongo.MongoServer
import de.bwaldvogel.mongo.backend.memory.MemoryBackend
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

private const val MONGO_HOST = "localhost"
private const val MONGO_PORT = 27017


class CarRepoTest {

    private var collection: MongoCollection<CarData>? = null
    private var client: MongoClient? = null
    private var server: MongoServer? = null

    @BeforeEach
    fun setUp() {
        val server = MongoServer(MemoryBackend())
        this.server = server

        server.bind(MONGO_HOST, MONGO_PORT)
        KMongo.createClient(MongoClientSettings.builder().applyConnectionString(ConnectionString(CAR_REPO_URL)).build())
        val client = KMongo.createClient(ConnectionString("mongodb://${MONGO_HOST}:${MONGO_PORT}"))
        this.client = client
        collection = client.getDatabase("testdb").getCollection<CarData>("cardata")
    }

    @AfterEach
    fun tearDown() {
        client?.close()
        server?.shutdown()
    }

    @Test
    fun testManufacturerAndYearFilter() {
        val collection = collection ?: throw NullPointerException("No mongo collection for test")

        val repo = CarMongoRepo(collection)
        repo.addCar(Cars.opelAstra)
        repo.addCar(Cars.peugeot308)
        Assertions.assertEquals(1, repo.getAllCars("Opel", null).size)
        Assertions.assertEquals(1, repo.getAllCars("Peugeot", null).size)
        Assertions.assertEquals(0, repo.getAllCars("Seat", null).size) // Non existing brand in collection

        Assertions.assertEquals(2, repo.getAllCars(null, null).size) // All results

        Assertions.assertEquals(1, repo.getAllCars(null, 2010).size)
        Assertions.assertEquals(1, repo.getAllCars(null, 2009).size)
        Assertions.assertEquals(0, repo.getAllCars(null, 2000).size) // Only exact year matches, not "younger than"
        Assertions.assertEquals(0, repo.getAllCars(null, 2020).size)

        Assertions.assertEquals(1, repo.getAllCars("Opel", 2010).size)
        Assertions.assertEquals(0, repo.getAllCars("Opel", 2009).size)
    }
}