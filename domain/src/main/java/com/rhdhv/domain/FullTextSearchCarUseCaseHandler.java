package com.rhdhv.domain;

import com.rhdhv.domain.common.DomainComponent;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.port.CarStorePort;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@DomainComponent
@RequiredArgsConstructor
public class FullTextSearchCarUseCaseHandler {

  private final CarStorePort carStorePort;


  public Page<Car> handle(final FullTextSearchCar useCase) {
    if (useCase.isEmpty()) {
      return this.carStorePort.findAll(useCase.pageRequest());
    }

    return this.carStorePort.search(useCase);
  }
}
