package com.rhdhv.domain.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Builder;

@Builder
public record Car(
    String id,
    String brand,
    String model,
    String version,
    Integer releaseYear,
    BigDecimal price,
    BigDecimal fuelConsumption,
    BigDecimal annualMaintenanceCost,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {

}
