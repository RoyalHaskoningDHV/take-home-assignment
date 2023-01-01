package com.rhdhv.infra.adapters.elasticsearch;


import static org.assertj.core.api.Assertions.assertThat;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.usecase.AddCarToStore;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import com.rhdhv.domain.usecase.RetrieveCarsByFilter;
import com.rhdhv.infra.ElasticsearchContainerInitializer;
import com.rhdhv.infra.adapters.elasticsearch.document.CarDocument;
import com.rhdhv.infra.adapters.elasticsearch.repository.CarESRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = ElasticsearchContainerInitializer.class)
class CarStoreESAdapterIT {

  @Autowired
  private CarStoreESAdapter carStoreESAdapter;

  @Autowired
  private CarESRepository carESRepository;

  @BeforeEach
  void setUp() {
    final var car = CarDocument.builder()
        .brand("Citroen")
        .model("C3")
        .version("elegance")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(20000.0))
        .fuelConsumption(BigDecimal.valueOf(0.66))
        .annualMaintenanceCost(BigDecimal.valueOf(5000.0))
        .build();

    final var honda = CarDocument.builder()
        .brand("Honda")
        .model("Fit")
        .version("C")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(21000.0))
        .fuelConsumption(BigDecimal.valueOf(0.55))
        .annualMaintenanceCost(BigDecimal.valueOf(400.0))
        .build();

    final var mercedes = CarDocument.builder()
        .brand("Mercedes")
        .model("A220")
        .version("A")
        .releaseYear(2022)
        .price(BigDecimal.valueOf(34000.0))
        .fuelConsumption(BigDecimal.valueOf(0.25))
        .annualMaintenanceCost(BigDecimal.valueOf(190.0))
        .build();

    final List<CarDocument> carDocuments = Stream.of(car, honda, mercedes).map(this.carESRepository::save).toList();
  }

  @AfterEach
  void tearDown() {
    this.carESRepository.deleteAll();
  }

  @Test
  void save() {
    //given
    final AddCarToStore addCarToStore = AddCarToStore.builder()
        .brand("Citroen")
        .model("C3")
        .version("elegance")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(20000))
        .fuelConsumption(BigDecimal.valueOf(0.66))
        .annualMaintenanceCost(BigDecimal.valueOf(100))
        .build();

    final Car car = this.carStoreESAdapter.save(addCarToStore);

    assertThat(car.id()).isNotNull();
    assertThat(car.createdAt()).isNotNull();
    assertThat(car.updatedAt()).isNotNull();

    assertThat(car).isNotNull()
        .returns("Citroen", Car::brand)
        .returns("C3", Car::model)
        .returns("elegance", Car::version)
        .returns(2018, Car::releaseYear)
        .returns(BigDecimal.valueOf(20000), Car::price)
        .returns(BigDecimal.valueOf(0.66), Car::fuelConsumption)
        .returns(BigDecimal.valueOf(100), Car::annualMaintenanceCost);
  }

  @Test
  void search() {
    //given
    final FullTextSearchCar fullTextSearchCar = new FullTextSearchCar("Citroen", PageRequest.of(0, 100));

    //when
    final Page<Car> search = this.carStoreESAdapter.search(fullTextSearchCar);

    //then
    final List<Car> content = search.getContent();

    assertThat(content).hasSize(1);

    assertThat(content.get(0))
        .returns("Citroen", Car::brand)
        .returns("C3", Car::model)
        .returns("elegance", Car::version)
        .returns(2018, Car::releaseYear)
        .returns(BigDecimal.valueOf(20000.0), Car::price)
        .returns(BigDecimal.valueOf(0.66), Car::fuelConsumption)
        .returns(BigDecimal.valueOf(5000.0), Car::annualMaintenanceCost);
  }

  @Test
  void findAll() {
    //given
    final PageRequest pageRequest = PageRequest.of(0, 100);

    //when
    final Page<Car> search = this.carStoreESAdapter.findAll(pageRequest);

    //then
    final List<Car> content = search.getContent();

    assertThat(content).hasSize(3);
  }

  @Test
  void retrieve() {
    //given
    final RetrieveCarsByFilter retrieveCarsByFilter = new RetrieveCarsByFilter(2018, "Citroen", PageRequest.of(0, 100));

    //when
    final Page<Car> search = this.carStoreESAdapter.retrieve(retrieveCarsByFilter);

    //then
    final List<Car> content = search.getContent();

    assertThat(content).hasSize(1);
  }


}