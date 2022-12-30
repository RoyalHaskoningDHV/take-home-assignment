package com.rhdhv.domain.service;

import com.rhdhv.domain.AddCarToStoreUseCaseHandler;
import com.rhdhv.domain.DeleteAllCarUseCaseHandler;
import com.rhdhv.domain.FullTextSearchCarUseCaseHandler;
import com.rhdhv.domain.RetrieveCarsByFilterUseCaseHandler;
import com.rhdhv.domain.common.DomainService;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.usecase.AddCarToStore;
import com.rhdhv.domain.usecase.DeleteAllCar;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import com.rhdhv.domain.usecase.RetrieveCarsByFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@DomainService
@RequiredArgsConstructor
public class CarStoreFacade {

  private final AddCarToStoreUseCaseHandler addCarToStoreUseCaseHandler;
  private final FullTextSearchCarUseCaseHandler fullTextSearchCarUseCaseHandler;
  private final RetrieveCarsByFilterUseCaseHandler retrieveCarsByFilterUseCaseHandler;
  private final DeleteAllCarUseCaseHandler deleteAllCarUseCaseHandler;

  public Car addCar(final AddCarToStore addCarToStore) {
    return this.addCarToStoreUseCaseHandler.handle(addCarToStore);
  }

  public Page<Car> search(final FullTextSearchCar searchCar) {
    return this.fullTextSearchCarUseCaseHandler.handle(searchCar);
  }

  public Page<Car> retrieve(final RetrieveCarsByFilter retrieveCarsByFilter) {
    return this.retrieveCarsByFilterUseCaseHandler.handle(retrieveCarsByFilter);
  }

  public void deleteAll() {
    final DeleteAllCar deleteAllCar = new DeleteAllCar();
    this.deleteAllCarUseCaseHandler.handle(deleteAllCar);
  }
}
