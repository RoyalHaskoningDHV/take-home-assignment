package com.rhdhv.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rhdhv.domain.adapters.FakeCarStoreAdapter;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class FullTextSearchCarTest {

  private FullTextSearchCarUseCaseHandler fullTextSearchCarUseCaseHandler;
  private FakeCarStoreAdapter fakeCarStoreAdapter;

  @BeforeEach
  void setUp() {
    this.fakeCarStoreAdapter = new FakeCarStoreAdapter();
    this.fullTextSearchCarUseCaseHandler = new FullTextSearchCarUseCaseHandler(this.fakeCarStoreAdapter);
  }

  @Test
  void aCarDealerShouldBeAbleToSearchCarsByYearAndBrand() {
    //given
    final FullTextSearchCar useCase = new FullTextSearchCar("Citroen", PageRequest.ofSize(1));

    final Car expectedCar = Car.builder()
        .id("Id_1")
        .brand("Citroen")
        .model("C3")
        .version("elegance")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(20000))
        .fuelConsumption(BigDecimal.valueOf(0.6))
        .annualMaintenanceCost(BigDecimal.valueOf(100))
        .build();

    //when
    final Page<Car> carPage = this.fullTextSearchCarUseCaseHandler.handle(useCase);

    // then
    assertThat(carPage).isNotNull();
    assertThat(carPage.getTotalPages()).isEqualTo(1);
    assertThat(carPage.getTotalElements()).isEqualTo(1);
    assertThat(carPage.getContent().get(0)).isEqualTo(expectedCar);
  }

  @Test
  void whenSearchTextIsEmptyThenReturnAllRecords() {
    //given
    final FullTextSearchCar useCase = new FullTextSearchCar(null, PageRequest.ofSize(1));

    final Car expectedCar = Car.builder()
        .id("Id_1")
        .brand("Citroen")
        .model("C3")
        .version("elegance")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(20000))
        .fuelConsumption(BigDecimal.valueOf(0.6))
        .annualMaintenanceCost(BigDecimal.valueOf(100))
        .build();

    //when
    final Page<Car> carPage = this.fullTextSearchCarUseCaseHandler.handle(useCase);

    // then
    assertThat(carPage).isNotNull();
    assertThat(carPage.getTotalPages()).isEqualTo(1);
    assertThat(carPage.getTotalElements()).isEqualTo(2);

    this.fakeCarStoreAdapter.contains(expectedCar);
  }
}
