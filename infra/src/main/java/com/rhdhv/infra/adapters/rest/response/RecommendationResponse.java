package com.rhdhv.infra.adapters.rest.response;

import com.rhdhv.domain.model.TotalAnnualCostOfCar;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;

public record RecommendationResponse(
    List<TotalAnnualCostOfCar> carsWithTotalAnnualCosts,
    BigDecimal givenAnnualCost,
    Integer page,
    Integer size,
    Integer totalPage,
    Long total) {

  public static RecommendationResponse from(final Page<TotalAnnualCostOfCar> page, final BigDecimal givenAnnualCost) {
    return new RecommendationResponse(
        page.getContent(),
        givenAnnualCost,
        page.getNumber(),
        page.getSize(),
        page.getTotalPages(),
        page.getTotalElements()
    );
  }

}
