package com.rhdhv.infra.adapters.elasticsearch;

import static com.rhdhv.infra.adapters.elasticsearch.ESConstants.CARS_INDEX;
import static com.rhdhv.infra.adapters.elasticsearch.ESConstants.RELEASE_YEAR_OR_BRAND_QUERY_STR;
import static com.rhdhv.infra.adapters.elasticsearch.ESUtils.prepareOffset;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest.Builder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.port.CarStorePort;
import com.rhdhv.domain.usecase.AddCarToStore;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import com.rhdhv.domain.usecase.RetrieveCarsByFilter;
import com.rhdhv.infra.adapters.elasticsearch.document.CarDocument;
import com.rhdhv.infra.adapters.elasticsearch.repository.CarESRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarStoreESAdapter implements CarStorePort {

  private final CarESRepository carESRepository;
  private final ElasticsearchClient elasticsearchClient;

  @Override
  public Car save(final AddCarToStore addCarToStore) {
    CarDocument carDocument = this.toDocument(addCarToStore);

    carDocument = this.carESRepository.save(carDocument);

    return carDocument.toModel();
  }

  @SneakyThrows
  @Override
  public Page<Car> search(final FullTextSearchCar searchCar) {
    final String queryString = RELEASE_YEAR_OR_BRAND_QUERY_STR.formatted(searchCar.text(), searchCar.text());

    final SearchRequest searchRequest = new Builder()
        .index(CARS_INDEX)
        .query(q -> q.queryString(qStr -> qStr.query(queryString)))
        .from(prepareOffset(searchCar.pageRequest()))
        .size(searchCar.pageRequest().getPageSize())
        .build();

    final SearchResponse<CarDocument> response = this.elasticsearchClient.search(searchRequest, CarDocument.class);

    return ESUtils.toPage(searchCar.pageRequest(), response, CarDocument::toModel);
  }

  @Override
  public Page<Car> findAll(final Pageable pageRequest) {
    final Page<CarDocument> cars = this.carESRepository.findAll(pageRequest);

    return this.toModelPage(cars);
  }


  @SneakyThrows
  @Override
  public Page<Car> retrieve(final RetrieveCarsByFilter filter) {
    final PageRequest pageRequest = filter.pageRequest();

    if (filter.year() != null && filter.brand() != null) {
      final Page<CarDocument> cars = this.carESRepository.findByReleaseYearAndBrand(filter.year(), filter.brand(), pageRequest);

      return this.toModelPage(cars);
    }

    if (filter.year() != null) {
      final Page<CarDocument> byReleaseYear = this.carESRepository.findByReleaseYear(filter.year(), pageRequest);

      return this.toModelPage(byReleaseYear);
    }

    if (filter.brand() != null) {
      final Page<CarDocument> byBrand = this.carESRepository.findByBrand(filter.brand(), pageRequest);

      return this.toModelPage(byBrand);
    }

    return this.findAll(pageRequest);
  }

  private PageImpl<Car> toModelPage(final Page<? extends CarDocument> page) {
    final List<Car> cars = page.getContent()
        .stream()
        .map(CarDocument::toModel)
        .toList();

    return new PageImpl<>(cars, page.getPageable(), page.getTotalElements());
  }

  @Override
  public void deleteAll() {
    this.carESRepository.deleteAll();
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
