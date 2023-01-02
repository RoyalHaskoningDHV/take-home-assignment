package com.rhdhv.domain;


import static org.assertj.core.api.Assertions.assertThat;

import com.rhdhv.domain.adapters.FakeCarStoreAdapter;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.usecase.AddCarToStore;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AddCarToStoreTest {

  private final AddCarToStoreUseCaseHandler addCarToStoreUseCaseHandler = new AddCarToStoreUseCaseHandler(
      new FakeCarStoreAdapter());

  @Test
  void aCarDealerShouldBeAbleToAddNewCarsToOwnStore() {
    //given
    final AddCarToStore useCase = AddCarToStore.builder()
        .brand("Citroen")
        .model("C3")
        .version("elegance")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(20000))
        .fuelConsumption(BigDecimal.valueOf(0.6))
        .annualMaintenanceCost(BigDecimal.valueOf(100))
        .build();

    //when
    final Car car = this.addCarToStoreUseCaseHandler.handle(useCase);

    // then
    assertThat(car).isNotNull()
        .returns("Id_1", Car::id)
        .returns("Citroen", Car::brand)
        .returns("C3", Car::model)
        .returns("elegance", Car::version)
        .returns(2018, Car::releaseYear)
        .returns(BigDecimal.valueOf(20000), Car::price)
        .returns(BigDecimal.valueOf(0.6), Car::fuelConsumption)
        .returns(BigDecimal.valueOf(100), Car::annualMaintenanceCost);
  }
}
