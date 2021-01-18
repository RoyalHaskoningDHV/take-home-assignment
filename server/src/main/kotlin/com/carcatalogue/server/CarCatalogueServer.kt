package com.carcatalogue.server

import com.carcatalogue.proto.*
import com.carcatalogue.server.repository.CarMongoRepo
import com.carcatalogue.server.repository.model.CarData
import com.carcatalogue.server.services.CarCatalogueService
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import io.grpc.Server
import io.grpc.ServerBuilder
import org.apache.logging.log4j.kotlin.Logging
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection

const val CAR_REPO_URL = "mongodb+srv://admin:admin@cluster0.qxqrv.mongodb.net/carrepo?retryWrites=true&w=majority";
const val CAR_REPO_DB_NAME = "carrepo"
const val CAR_COLLECTION_NAME = "cars"

class CarCatalogueServer constructor(private val port: Int): Logging {

    private val mongoClient = KMongo.createClient(MongoClientSettings.builder().applyConnectionString(ConnectionString(CAR_REPO_URL)).build())
    private val database = mongoClient.getDatabase(CAR_REPO_DB_NAME)

    private val server: Server = ServerBuilder
            .forPort(port)
            .addService(CarCatalogueService(CarMongoRepo(database.getCollection<CarData>(CAR_COLLECTION_NAME))))
            .build()


    fun start() {
        server.start()
        logger.info("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
                Thread {
                    logger.info("*** shutting down gRPC server since JVM is shutting down")
                    this@CarCatalogueServer.stop()
                    logger.info("*** server shut down")
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
