package com.rhdhv.domain.port;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.usecase.AddCarToStore;

public interface CarStorePort {

  Car save(AddCarToStore addCarToStore);
}
