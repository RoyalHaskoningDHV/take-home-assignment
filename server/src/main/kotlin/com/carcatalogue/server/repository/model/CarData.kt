package com.carcatalogue.server.repository.model

import org.litote.kmongo.Data

@Data
data class CarData(
    val model: String,
    val manufacturer: String,
    val version: String,
    val releaseYear: Int,
    val priceInCents: Int,
    val fuelConsumption: Double,
    val maintenanceCostInCents: Int)