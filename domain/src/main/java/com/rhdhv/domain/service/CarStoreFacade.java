package com.rhdhv.domain.service;

import com.rhdhv.domain.AddCarToStoreUseCaseHandler;
import com.rhdhv.domain.common.DomainService;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.usecase.AddCarToStore;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class CarStoreFacade {

  private final AddCarToStoreUseCaseHandler addCarToStoreUseCaseHandler;

  public Car addCar(final AddCarToStore addCarToStore) {
    return this.addCarToStoreUseCaseHandler.handle(addCarToStore);
  }


}
