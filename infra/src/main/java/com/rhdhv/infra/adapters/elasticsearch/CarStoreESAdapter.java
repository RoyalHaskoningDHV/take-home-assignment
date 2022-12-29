package com.rhdhv.infra.adapters.elasticsearch;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.port.CarStorePort;
import com.rhdhv.domain.usecase.AddCarToStore;
import com.rhdhv.infra.adapters.elasticsearch.document.CarDocument;
import com.rhdhv.infra.adapters.elasticsearch.repository.CarESRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarStoreESAdapter implements CarStorePort {

  private final CarESRepository carESRepository;

  @Override
  public Car save(final AddCarToStore addCarToStore) {
    CarDocument carDocument = this.toDocument(addCarToStore);

    carDocument = this.carESRepository.save(carDocument);

    return carDocument.toModel();
  }

  private CarDocument toDocument(final AddCarToStore addCarToStore) {
    return CarDocument.builder()
        .brand(addCarToStore.brand())
        .model(addCarToStore.model())
        .version(addCarToStore.version())
        .releaseYear(addCarToStore.releaseYear())
        .price(addCarToStore.price())
        .fuelConsumption(addCarToStore.fuelConsumption())
        .annualMaintenanceCost(addCarToStore.annualMaintenanceCost())
        .build();
  }

}
