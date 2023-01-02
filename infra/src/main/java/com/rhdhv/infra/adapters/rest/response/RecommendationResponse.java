package com.rhdhv.infra.adapters.rest.response;

import com.rhdhv.domain.model.TotalAnnualCostOfCar;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;

public record RecommendationResponse(
    BigDecimal totalFuelCost,
    BigDecimal totalFuelCostPerYear,
    int totalTravelDistance,
    int totalTravelDistancePerYear,
    List<TotalAnnualCostOfCar> carsWithTotalAnnualCosts,
    Integer page,
    Integer size,
    Integer totalPage,
    Long total) {

  public static RecommendationResponse from(
      final Page<TotalAnnualCostOfCar> page, final BigDecimal totalFuelCost,
      final BigDecimal totalFuelCostPerYear,
      final int totalTravelDistance,
      final int totalTravelDistancePerYear) {

    return new RecommendationResponse(
        totalFuelCost,
        totalFuelCostPerYear,
        totalTravelDistance,
        totalTravelDistancePerYear,
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalPages(),
        page.getTotalElements()
    );
  }

}
