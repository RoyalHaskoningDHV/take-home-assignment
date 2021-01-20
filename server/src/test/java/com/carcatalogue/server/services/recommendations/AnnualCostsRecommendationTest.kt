package com.carcatalogue.server.services.recommendations

import com.carcatalogue.server.mockdata.Cars
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AnnualCostsRecommendationTest {
    @Test
    fun testRecommendationCalculation() {
        Assertions.assertEquals(268158, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.mercedesB, 100, 1000))
        Assertions.assertEquals(205632, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.mercedesB, 100, 10))

        Assertions.assertEquals(229552, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.opelAstra, 100, 1000))
        Assertions.assertEquals(140896, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.opelAstra, 100, 10))

        Assertions.assertEquals(242922, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.peugeot308, 100, 1000))
        Assertions.assertEquals(165779, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.peugeot308, 100, 10))
    }

    @Test
    fun testRecommendationCalculationWithEdgeCaseInput() {
        Assertions.assertEquals(165000, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.peugeot308, 0, 0))
        Assertions.assertEquals(205000, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.mercedesB, 0, 0))
        Assertions.assertEquals(140000, AnnualCostsRecommendation()
            .getAnnualCostsInCents(Cars.opelAstra, 0, 0))
    }

    @Test
    fun testRecommendationSorting() {
        val sort1 = AnnualCostsRecommendation()
            .sortByAnnualCosts(listOf(Cars.mercedesB, Cars.peugeot308, Cars.opelAstra), 100, 1000)
        Assertions.assertEquals(Cars.opelAstra, sort1[0].car)
        Assertions.assertEquals(Cars.peugeot308, sort1[1].car)
        Assertions.assertEquals(Cars.mercedesB, sort1[2].car)

        val sort2 = AnnualCostsRecommendation()
            .sortByAnnualCosts(listOf(Cars.mercedesB, Cars.peugeot308, Cars.opelAstra), 230, 15000)
        Assertions.assertEquals(Cars.mercedesB, sort2[0].car)
        Assertions.assertEquals(Cars.peugeot308, sort2[1].car)
        Assertions.assertEquals(Cars.opelAstra, sort2[2].car)
    }

    @Test
    fun testRecommendationSortingWithEdgeCaseInput() {
        val sort1 = AnnualCostsRecommendation()
            .sortByAnnualCosts(listOf(Cars.mercedesB, Cars.peugeot308, Cars.opelAstra), 0, 0)
        Assertions.assertEquals(Cars.opelAstra, sort1[0].car)
        Assertions.assertEquals(Cars.peugeot308, sort1[1].car)
        Assertions.assertEquals(Cars.mercedesB, sort1[2].car)
    }

    @Test
    fun testRecommendationSortingWithoutCars() {
        Assertions.assertEquals(0, AnnualCostsRecommendation()
            .sortByAnnualCosts(listOf(), 100, 1000).size)
    }
}