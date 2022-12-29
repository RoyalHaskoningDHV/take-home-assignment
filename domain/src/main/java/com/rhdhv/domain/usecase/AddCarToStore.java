package com.rhdhv.domain.usecase;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record AddCarToStore(
    String brand,
    String model,
    String version,
    Integer releaseYear,
    BigDecimal price,
    BigDecimal fuelConsumption,
    BigDecimal annualMaintenanceCost
) {

}
