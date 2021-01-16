package com.carcatalogue.server.repository.model

data class CarData(
    val model: String,
    val manufacturer: String,
    val version: String,
    val releaseYear: Int,
    val priceInCents: Int,
    val fuelConsumption: Double,
    val maintenanceCostInCents: Int)