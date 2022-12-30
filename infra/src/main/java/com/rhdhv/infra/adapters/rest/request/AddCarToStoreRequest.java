package com.rhdhv.infra.adapters.rest.request;

import com.rhdhv.domain.usecase.AddCarToStore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record AddCarToStoreRequest(
    @NotEmpty String brand,
    @NotEmpty String model,
    @NotEmpty String version,
    @NotNull @Positive Integer releaseYear,
    @NotNull @Positive BigDecimal price,
    @NotNull @Positive BigDecimal fuelConsumption,
    @NotNull @Positive BigDecimal annualMaintenanceCost
) {

  public AddCarToStore toModel() {
    return AddCarToStore.builder()
        .brand(this.brand)
        .model(this.model)
        .version(this.version)
        .releaseYear(this.releaseYear)
        .price(this.price)
        .fuelConsumption(this.fuelConsumption)
        .annualMaintenanceCost(this.annualMaintenanceCost)
        .build();
  }
}
