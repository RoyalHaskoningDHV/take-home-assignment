package com.rhdhv.infra.adapters.rest.response;

import com.rhdhv.domain.model.Car;
import java.util.List;
import org.springframework.data.domain.Page;

public record CarsResponse(List<CarResponse> cars, Integer page, Integer size, Integer totalPage, Long total) {

  public static CarsResponse from(final Page<Car> carsPage) {
    final List<CarResponse> cars = carsPage.stream()
        .map(CarResponse::from)
        .toList();

    return new CarsResponse(cars,
        carsPage.getNumber(),
        carsPage.getSize(),
        carsPage.getTotalPages(),
        carsPage.getTotalElements()
    );
  }
}
