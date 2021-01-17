package com.carcatalogue.server.mockdata

import com.carcatalogue.server.repository.model.CarData

object Cars {
    val mercedesB = CarData(model = "B-Klasse",
        manufacturer = "Mercedes",
        version = "180",
        releaseYear = 2008,
        priceInCents = 3500000,
        fuelConsumption = 10.0,
        maintenanceCostInCents = 30000
    )
    val opelAstra = CarData(model = "Astra",
        manufacturer = "Opel",
        version = "1.4 Turbo",
        releaseYear = 2010,
        priceInCents = 2000000,
        fuelConsumption = 13.4,
        maintenanceCostInCents = 40000
    )
    val peugeot308 = CarData(model = "308",
        manufacturer = "Peugeot",
        version = "ST",
        releaseYear = 2009,
        priceInCents = 2300000,
        fuelConsumption = 15.4,
        maintenanceCostInCents = 50000
    )
}