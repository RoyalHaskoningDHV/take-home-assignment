package com.rhdhv.domain;

import com.rhdhv.domain.common.DomainComponent;
import com.rhdhv.domain.model.TotalAnnualCostOfCar;
import com.rhdhv.domain.port.CarRecommendationPort;
import com.rhdhv.domain.usecase.RecommendCarsByLowestTotalAnnualCosts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@DomainComponent
@RequiredArgsConstructor
public class RecommendCarUseCaseHandler {

  private final CarRecommendationPort carRecommendationPort;

  public Page<TotalAnnualCostOfCar> handle(final RecommendCarsByLowestTotalAnnualCosts useCase) {
    return this.carRecommendationPort.findCarsByLowestTotalAnnualCost(useCase);
  }

}
