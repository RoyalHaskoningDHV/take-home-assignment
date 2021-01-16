package com.carcatalogue.server.repository

import com.carcatalogue.server.repository.model.CarData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CarRepoTest {

    val opelAstra = CarData(model = "Astra",
                            manufacturer = "Opel",
                            version = "1.4 Turbo",
                            releaseYear = 2010,
                            priceInCents = 20000,
                            fuelConsumption = 13.4,
                            maintenanceCostInCents = 40000
    )

    @Test
    fun testManufacturerAndYearFilter() {
//        Assertions.assertEquals(true, filterByManufacturerAndYear(opelAstra, "Opel", null))
//        Assertions.assertEquals(true, filterByManufacturerAndYear(opelAstra, null, 2010))
//        Assertions.assertEquals(false, filterByManufacturerAndYear(opelAstra, null, 2009))
//        Assertions.assertEquals(false, filterByManufacturerAndYear(opelAstra, "Peugeot", null))
    }
}