package com.rhdhv.domain;

import com.rhdhv.domain.common.DomainComponent;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.port.CarStorePort;
import com.rhdhv.domain.usecase.RetrieveCarsByFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@DomainComponent
@RequiredArgsConstructor
public class RetrieveCarsByFilterUseCaseHandler {

  private final CarStorePort carStorePort;

  public Page<Car> handle(final RetrieveCarsByFilter retrieveCarsByFilter) {
    return this.carStorePort.retrieve(retrieveCarsByFilter);
  }
}
