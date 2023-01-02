package com.rhdhv.domain.adapters;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.port.CarStorePort;
import com.rhdhv.domain.usecase.AddCarToStore;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import com.rhdhv.domain.usecase.RetrieveCarsByFilter;
import java.math.BigDecimal;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class FakeCarStoreAdapter implements CarStorePort {

  private final List<Car> cars = List.of(
      Car.builder()
          .id("Id_1")
          .brand("Citroen")
          .model("C3")
          .version("elegance")
          .releaseYear(2018)
          .price(BigDecimal.valueOf(20000))
          .fuelConsumption(BigDecimal.valueOf(0.6))
          .annualMaintenanceCost(BigDecimal.valueOf(100))
          .build()
      ,
      Car.builder()
          .id("Id_2")
          .brand("Honda")
          .model("Civic")
          .version("elegance")
          .releaseYear(2020)
          .price(BigDecimal.valueOf(22000))
          .fuelConsumption(BigDecimal.valueOf(0.7))
          .annualMaintenanceCost(BigDecimal.valueOf(300))
          .build()
  );

  @Override
  public Car save(final AddCarToStore addCarToStore) {
    return Car.builder()
        .id("Id_1")
        .brand(addCarToStore.brand())
        .model(addCarToStore.model())
        .version(addCarToStore.version())
        .releaseYear(addCarToStore.releaseYear())
        .price(addCarToStore.price())
        .fuelConsumption(addCarToStore.fuelConsumption())
        .annualMaintenanceCost(addCarToStore.annualMaintenanceCost())
        .build();
  }

  @Override
  public Page<Car> search(final FullTextSearchCar fullTextSearchCar) {
    return new PageImpl<>(this.cars.subList(0, 1), Pageable.ofSize(10), this.cars.size());
  }

  @Override
  public Page<Car> findAll(final Pageable pageRequest) {
    return new PageImpl<>(this.cars, Pageable.ofSize(2), this.cars.size());
  }

  @Override
  public Page<Car> retrieve(RetrieveCarsByFilter retrieveCarsByFilter) {
    return null;
  }

  @Override
  public void deleteAll() {

  }

  public void contains(final Car... car) {
    Assertions.assertThat(this.cars).contains(car);
  }
}
