package com.rhdhv.infra.adapters.web;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.service.CarStoreFacade;
import com.rhdhv.infra.adapters.web.request.AddCarToStoreRequest;
import com.rhdhv.infra.adapters.web.response.CarResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
