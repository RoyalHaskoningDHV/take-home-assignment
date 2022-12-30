package com.rhdhv.domain;

import com.rhdhv.domain.common.DomainComponent;
import com.rhdhv.domain.port.CarStorePort;
import com.rhdhv.domain.usecase.DeleteAllCar;
import lombok.RequiredArgsConstructor;

@DomainComponent
@RequiredArgsConstructor
public class DeleteAllCarUseCaseHandler {

  private final CarStorePort carStorePort;

  public void handle(final DeleteAllCar deleteAllCar) {

    this.carStorePort.deleteAll();
  }
}
