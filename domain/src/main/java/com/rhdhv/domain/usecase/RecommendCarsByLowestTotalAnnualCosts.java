package com.rhdhv.domain.usecase;


import java.math.BigDecimal;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record RecommendCarsByLowestTotalAnnualCosts(BigDecimal fuelConsumption, int travelDistancePerMonth, int period,
                                                    Pageable pageRequest) {

  private static final int MONTHS_12 = 12;

  public BigDecimal totalFuelCost() {
    return this.fuelConsumption.multiply(BigDecimal.valueOf(this.totalTravelDistance()));
  }

  public int totalTravelDistance() {
    return this.travelDistancePerMonth * MONTHS_12 * this.period();
  }
}
