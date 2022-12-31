package com.rhdhv.domain.port;

import com.rhdhv.domain.model.TotalAnnualCostOfCar;
import com.rhdhv.domain.usecase.RecommendCarsByLowestTotalAnnualCosts;
import org.springframework.data.domain.Page;

public interface CarRecommendationPort {

  Page<TotalAnnualCostOfCar> findCarsByLowestTotalAnnualCost(RecommendCarsByLowestTotalAnnualCosts useCase);
}
