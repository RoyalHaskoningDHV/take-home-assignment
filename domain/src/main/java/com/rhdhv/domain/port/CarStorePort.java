package com.rhdhv.domain.port;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.usecase.AddCarToStore;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import com.rhdhv.domain.usecase.RetrieveCarsByFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarStorePort {

  Car save(AddCarToStore addCarToStore);

  Page<Car> search(FullTextSearchCar fullTextSearchCar);

  Page<Car> findAll(Pageable pageRequest);

  Page<Car> retrieve(RetrieveCarsByFilter retrieveCarsByFilter);

  void deleteAll();
}
