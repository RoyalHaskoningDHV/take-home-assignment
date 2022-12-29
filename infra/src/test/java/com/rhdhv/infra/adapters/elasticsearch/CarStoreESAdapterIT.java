package com.rhdhv.infra.adapters.elasticsearch;


import static org.assertj.core.api.Assertions.assertThat;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.usecase.AddCarToStore;
import com.rhdhv.infra.AbstractIT;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CarStoreESAdapterIT extends AbstractIT {

  @Autowired
  private CarStoreESAdapter carStoreESAdapter;

  @BeforeEach
  void setUp() {
    ELASTICSEARCH_CONTAINER.start();
  }

  @AfterEach
  void tearDown() {
    ELASTICSEARCH_CONTAINER.stop();
  }

  @Test
  void save() {
    //given
    final AddCarToStore addCarToStore = AddCarToStore.builder()
        .brand("Citroen")
        .model("C3")
        .version("elegance")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(20000))
        .fuelConsumption(BigDecimal.valueOf(0.66))
        .annualMaintenanceCost(BigDecimal.valueOf(100))
        .build();

    final Car car = this.carStoreESAdapter.save(addCarToStore);

    assertThat(car.id()).isNotNull();
    assertThat(car.createdAt()).isNotNull();
    assertThat(car.updatedAt()).isNotNull();

    assertThat(car).isNotNull()
        .returns("Citroen", Car::brand)
        .returns("C3", Car::model)
        .returns("elegance", Car::version)
        .returns(2018, Car::releaseYear)
        .returns(BigDecimal.valueOf(20000), Car::price)
        .returns(BigDecimal.valueOf(0.66), Car::fuelConsumption)
        .returns(BigDecimal.valueOf(100), Car::annualMaintenanceCost);
  }

}