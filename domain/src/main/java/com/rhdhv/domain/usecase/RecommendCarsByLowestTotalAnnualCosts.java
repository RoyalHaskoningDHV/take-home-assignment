package com.rhdhv.domain.usecase;


import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

@Builder
public record RecommendCarsByLowestTotalAnnualCosts(
    BigDecimal fuelConsumption,
    int travelDistancePerMonth,
    int period,
    Pageable pageRequest) {

  private static final int MONTHS_12 = 12;

  public BigDecimal totalFuelCost() {
    return this.fuelConsumption.multiply(BigDecimal.valueOf(this.totalTravelDistance()));
  }

  public BigDecimal totalFuelCostPerYear() {
    return this.fuelConsumption.multiply(BigDecimal.valueOf(this.totalTravelDistancePerYear()));
  }

  public BigDecimal totalFuelCostBy(@NonNull final BigDecimal fuelConsumption) {
    return fuelConsumption.multiply(BigDecimal.valueOf(this.totalTravelDistancePerYear()));
  }

  public BigDecimal totAnnualCostBy(final BigDecimal fuelConsumption, final BigDecimal annualMaintenanceCost) {
    return this.totalFuelCostBy(fuelConsumption).add(annualMaintenanceCost);
  }

  public int totalTravelDistance() {
    return this.totalTravelDistancePerYear() * this.period();
  }

  public int totalTravelDistancePerYear() {
    return this.travelDistancePerMonth * MONTHS_12;
  }

}
