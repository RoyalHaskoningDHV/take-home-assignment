/*
 * Copyright 2020 gRPC authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.carcatalogue.server

import com.carcatalogue.proto.*
import io.grpc.Server
import io.grpc.ServerBuilder

class CarCatalogueServer constructor(private val port: Int) {
    val server: Server = ServerBuilder
            .forPort(port)
            .addService(CarCatalogueService())
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

    private class CarCatalogueService : SearchCarGrpcKt.SearchCarCoroutineImplBase() {

        override suspend fun search(request: SearchRequest): SearchReply {
            return SearchReply.newBuilder().addCars(Car.newBuilder().setManufacturer(request.manufacturer).setProductionYear("2010").build()).build()
        }
    }
}

fun main() {
    val port = 50051
    val server = CarCatalogueServer(port)
    server.start()
    server.blockUntilShutdown()
}
