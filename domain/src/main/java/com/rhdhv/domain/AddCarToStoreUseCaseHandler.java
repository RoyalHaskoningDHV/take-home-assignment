package com.rhdhv.domain;

import com.rhdhv.domain.common.DomainComponent;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.port.CarStorePort;
import com.rhdhv.domain.usecase.AddCarToStore;
import lombok.RequiredArgsConstructor;

@DomainComponent
@RequiredArgsConstructor
public class AddCarToStoreUseCaseHandler {

  private final CarStorePort carStorePort;

  public Car handle(final AddCarToStore addCarToStore) {
    return this.carStorePort.save(addCarToStore);
  }

}
