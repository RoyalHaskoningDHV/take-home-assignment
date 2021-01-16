package com.carcatalogue.server

import com.carcatalogue.proto.*
import com.carcatalogue.server.repository.CarMongoRepo
import com.carcatalogue.server.repository.CarRepo
import com.carcatalogue.server.repository.model.CarData
import com.carcatalogue.server.services.CarCatalogueService
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import io.grpc.Server
import io.grpc.ServerBuilder
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

const val CAR_REPO_URL = "mongodb+srv://admin:admin@cluster0.qxqrv.mongodb.net/carrepo?retryWrites=true&w=majority";
const val CAR_REPO_DB_NAME = "carrepo"

class CarCatalogueServer constructor(private val port: Int) {

    private val mongoClient = KMongo.createClient(MongoClientSettings.builder().applyConnectionString(ConnectionString(CAR_REPO_URL)).build())
    private val database = mongoClient.getDatabase(CAR_REPO_DB_NAME)

    private val server: Server = ServerBuilder
            .forPort(port)
            .addService(CarCatalogueService(CarMongoRepo(database.getCollection<CarData>("cars"))))
            .build()


    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
                Thread {
                    println("*** shutting down gRPC server since JVM is shutting down")
                    this@CarCatalogueServer.stop()
                    println("*** server shut down")
                }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main() {
    val port = 50051
    val server = CarCatalogueServer(port)
    server.start()
    server.blockUntilShutdown()
}
