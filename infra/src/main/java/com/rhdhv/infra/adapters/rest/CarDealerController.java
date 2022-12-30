package com.rhdhv.infra.adapters.rest;

import static com.rhdhv.infra.adapters.rest.PaginationConstants.DEFAULT_PAGE_SIZE;
import static com.rhdhv.infra.adapters.rest.PaginationConstants.FIRST_PAGE;
import static com.rhdhv.infra.adapters.rest.PaginationConstants.PRICE;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.service.CarStoreFacade;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import com.rhdhv.domain.usecase.RetrieveCarsByFilter;
import com.rhdhv.infra.adapters.rest.request.AddCarToStoreRequest;
import com.rhdhv.infra.adapters.rest.response.CarResponse;
import com.rhdhv.infra.adapters.rest.response.CarsResponse;
import jakarta.validation.Valid;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/car-dealer")
public class CarDealerController {

  private final CarStoreFacade carStoreFacade;

  @PostMapping
  public ResponseEntity<CarResponse> addCarToStore(@RequestBody @Valid final AddCarToStoreRequest request) {

    final Car car = this.carStoreFacade.addCar(request.toModel());

    return ResponseEntity.status(HttpStatus.CREATED).body(CarResponse.from(car));
  }

  @GetMapping
  public ResponseEntity<CarsResponse> retrieve(
      @RequestParam(value = "year", required = false) final Integer year,
      @RequestParam(value = "brand", required = false) final String brand,
      @RequestParam(value = "page", required = false) final Integer page,
      @RequestParam(value = "size", required = false) final Integer size,
      @RequestParam(value = "sort", required = false) final Sort.Direction direction,
      @RequestParam(value = "props", required = false) final String properties) {

    final PageRequest pageRequest = PageRequest.of(
        Objects.requireNonNullElse(page, FIRST_PAGE),
        Objects.requireNonNullElse(size, DEFAULT_PAGE_SIZE),
        Objects.requireNonNullElse(direction, Direction.ASC),
        Objects.requireNonNullElse(properties, PRICE)
    );

    final RetrieveCarsByFilter retrieveCarsByFilter = new RetrieveCarsByFilter(year, brand, pageRequest);

    final Page<Car> cars = this.carStoreFacade.retrieve(retrieveCarsByFilter);

    return ResponseEntity.ok(CarsResponse.from(cars));
  }


  @GetMapping("/search")
  public ResponseEntity<CarsResponse> search(
      @RequestParam(value = "query", required = false) final String query,
      @RequestParam(value = "page", required = false) final Integer page,
      @RequestParam(value = "size", required = false) final Integer size,
      @RequestParam(value = "sort", required = false) final Sort.Direction direction,
      @RequestParam(value = "props", required = false) final String properties) {

    final PageRequest pageRequest = PageRequest.of(
        Objects.requireNonNullElse(page, FIRST_PAGE),
        Objects.requireNonNullElse(size, DEFAULT_PAGE_SIZE),
        Objects.requireNonNullElse(direction, Direction.ASC),
        Objects.requireNonNullElse(properties, PRICE)
    );

    final FullTextSearchCar fullTextSearchCar = new FullTextSearchCar(query, pageRequest);

    final Page<Car> cars = this.carStoreFacade.search(fullTextSearchCar);

    return ResponseEntity.ok(CarsResponse.from(cars));
  }
}
