package com.rhdhv.infra.adapters.web.response;

import com.rhdhv.domain.model.Car;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Builder;

@Builder
public record CarResponse(
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

  public static CarResponse from(final Car car) {
    return builder()
        .id(car.id())
        .brand(car.brand())
        .model(car.model())
        .version(car.version())
        .releaseYear(car.releaseYear())
        .price(car.price())
        .fuelConsumption(car.fuelConsumption())
        .annualMaintenanceCost(car.annualMaintenanceCost())
        .createdAt(car.createdAt())
        .updatedAt(car.updatedAt())
        .build();
  }
}
