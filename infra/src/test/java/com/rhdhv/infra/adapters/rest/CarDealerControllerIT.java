package com.rhdhv.infra.adapters.rest;

import static com.rhdhv.infra.adapters.rest.PaginationConstants.DEFAULT_PAGE_SIZE;
import static com.rhdhv.infra.adapters.rest.PaginationConstants.FIRST_PAGE;
import static org.assertj.core.api.Assertions.assertThat;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.service.CarStoreFacade;
import com.rhdhv.infra.AbstractIT;
import com.rhdhv.infra.ElasticsearchContainerInitializer;
import com.rhdhv.infra.adapters.rest.request.AddCarToStoreRequest;
import com.rhdhv.infra.adapters.rest.response.CarResponse;
import com.rhdhv.infra.adapters.rest.response.CarsResponse;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(initializers = ElasticsearchContainerInitializer.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarDealerControllerIT extends AbstractIT {

  @Autowired
  protected CarStoreFacade carStoreFacade;

  private Car citroen;
  private Car honda;

  @BeforeEach
  void setUp() {
    final var car = AddCarToStoreRequest.builder()
        .brand("Citroen")
        .model("C3")
        .version("elegance")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(20000.0))
        .fuelConsumption(BigDecimal.valueOf(0.66))
        .annualMaintenanceCost(BigDecimal.valueOf(5000.0))
        .build();

    final var honda = AddCarToStoreRequest.builder()
        .brand("Honda")
        .model("Fit")
        .version("C")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(21000.0))
        .fuelConsumption(BigDecimal.valueOf(0.55))
        .annualMaintenanceCost(BigDecimal.valueOf(400.0))
        .build();

    this.citroen = this.carStoreFacade.addCar(car.toModel());
    this.honda = this.carStoreFacade.addCar(honda.toModel());
  }

  @AfterEach
  void tearDown() {
    this.carStoreFacade.deleteAll();
  }


  @Test
  void whenPostAddCarToStoreThen201() {
    //given
    final AddCarToStoreRequest request = AddCarToStoreRequest.builder()
        .brand("Citroen")
        .model("C3")
        .version("elegance")
        .releaseYear(2018)
        .price(BigDecimal.valueOf(20000))
        .fuelConsumption(BigDecimal.valueOf(0.66))
        .annualMaintenanceCost(BigDecimal.valueOf(500))
        .build();
    //when
    final ResponseEntity<CarResponse> response = this.testRestTemplate.exchange(
        new RequestEntity<>(request, HttpMethod.POST, URI.create("/api/v1/car-dealer")),
        CarResponse.class
    );

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.CREATED, ResponseEntity::getStatusCode);
    // then body
    assertThat(response.getBody()).isNotNull()
        .returns("Citroen", CarResponse::brand)
        .returns("C3", CarResponse::model)
        .returns("elegance", CarResponse::version)
        .returns(2018, CarResponse::releaseYear)
        .returns(BigDecimal.valueOf(20000), CarResponse::price)
        .returns(BigDecimal.valueOf(0.66), CarResponse::fuelConsumption)
        .returns(BigDecimal.valueOf(500), CarResponse::annualMaintenanceCost);

    assertThat(response.getBody().id()).isNotNull();
  }

  @Test
  void whenPostInvalidAddCarToStoreThen400() {
    //given
    final AddCarToStoreRequest invalidRequest = AddCarToStoreRequest.builder()
        .brand(null)
        .model("")
        .version("")
        .releaseYear(-10)
        .price(BigDecimal.valueOf(-52))
        .fuelConsumption(null)
        .annualMaintenanceCost(null)
        .build();

    //when
    final ResponseEntity<ProblemDetail> response = this.testRestTemplate.exchange(
        new RequestEntity<>(invalidRequest, HttpMethod.POST, URI.create("/api/v1/car-dealer")),
        ProblemDetail.class
    );

    //then status
    assertThat(response).isNotNull()
        .returns(HttpStatus.BAD_REQUEST, ResponseEntity::getStatusCode);

    //then headers
    assertThat(response.getHeaders().getContentType())
        .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);

    // then body
    final ProblemDetail responseBody = response.getBody();
    assertThat(responseBody).isNotNull();

    assertThat(responseBody.getStatus()).isEqualTo(400);

    assertThat(responseBody.getDetail())
        .contains("Wrong fields",
            "annualMaintenanceCost must not be null",
            "price must be greater than 0",
            "version must not be empty",
            "model must not be empty",
            "releaseYear must be greater than 0",
            "fuelConsumption must not be null",
            "brand must not be empty");

  }

  @Test
  void retrieveByReleaseYearThenReturnMatchedWith200() {

    //when
    final UriComponentsBuilder year = UriComponentsBuilder.fromUri(URI.create("/api/v1/car-dealer"))
        .queryParam("year", 2018);

    final ResponseEntity<CarsResponse> response = this.testRestTemplate.getForEntity(year.toUriString(), CarsResponse.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.OK, ResponseEntity::getStatusCode);

    // then body
    final CarsResponse carsResponse = response.getBody();
    verifyResponse(carsResponse, this.citroen, this.honda);
  }


  @Test
  void retrieveByBrandThenReturnMatchedWith200() {
    //when
    final UriComponentsBuilder year = UriComponentsBuilder.fromUri(URI.create("/api/v1/car-dealer"))
        .queryParam("brand", "Citroen");

    final ResponseEntity<CarsResponse> response = this.testRestTemplate.getForEntity(year.toUriString(), CarsResponse.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.OK, ResponseEntity::getStatusCode);

    // then body
    final CarsResponse carsResponse = response.getBody();
    verifyResponse(carsResponse, this.citroen);
  }


  @Test
  void withoutQueryParamsThenReturnAllWith200() {
    //when
    final ResponseEntity<CarsResponse> response = this.testRestTemplate.getForEntity(URI.create("/api/v1/car-dealer"),
        CarsResponse.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.OK, ResponseEntity::getStatusCode);

    // then body
    final CarsResponse carsResponse = response.getBody();

    verifyResponse(carsResponse, this.citroen, this.honda);
  }

  @Test
  void retrieve2QueryParamsThenReturnAllWith200() {
    //when
    final ResponseEntity<CarsResponse> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer?brand=Honda&year=2018"),
        CarsResponse.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.OK, ResponseEntity::getStatusCode);

    // then body
    final CarsResponse carsResponse = response.getBody();

    verifyResponse(carsResponse, this.honda);
  }

  @Test
  void retrieveByInvalidQueryParamsThenReturn400() {
    //when
    final ResponseEntity<ProblemDetail> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer?brand=Honda&year=2018&page=0&size=20000"),
        ProblemDetail.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.BAD_REQUEST, ResponseEntity::getStatusCode);

    // then body
    final var problemDetail = response.getBody();

    assertThat(problemDetail).returns(400, ProblemDetail::getStatus)
        .returns("com.rhdhv.infra.adapters.rest.CarDealerController retrieve.size: must be less than or equal to 2000",
            ProblemDetail::getDetail);
  }


  @Test
  void fullTextSearchByYearThenReturnAllWith200() {
    //when
    final ResponseEntity<CarsResponse> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer/search?year=2018"),
        CarsResponse.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.OK, ResponseEntity::getStatusCode);

    // then body
    final CarsResponse carsResponse = response.getBody();

    verifyResponse(carsResponse, this.citroen, this.honda);
  }


  @Test
  void fullTextSearchByBrandThenReturnAllWith200() {
    //when
    final ResponseEntity<CarsResponse> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer/search?query=Citroen"),
        CarsResponse.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.OK, ResponseEntity::getStatusCode);

    // then body
    final CarsResponse carsResponse = response.getBody();

    verifyResponse(carsResponse, this.citroen);
  }

  @Test
  void fullTextSearchWithoutQueryThenReturnAllWith200() {
    //when
    final ResponseEntity<CarsResponse> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer/search"),
        CarsResponse.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.OK, ResponseEntity::getStatusCode);

    // then body
    final CarsResponse carsResponse = response.getBody();

    verifyResponse(carsResponse, this.citroen, this.honda);
  }


  private static void verifyResponse(final CarsResponse carsResponse, final Car... expected) {
    assertThat(carsResponse).isNotNull()
        .returns((long) expected.length, CarsResponse::total)
        .returns(FIRST_PAGE, CarsResponse::page)
        .returns(1, CarsResponse::totalPage)
        .returns(DEFAULT_PAGE_SIZE, CarsResponse::size);

    for (int i = 0; i < expected.length; i++) {

      final CarResponse actual = carsResponse.cars().get(i);
      assertThat(actual)
          .returns(expected[i].brand(), CarResponse::brand)
          .returns(expected[i].model(), CarResponse::model)
          .returns(expected[i].version(), CarResponse::version)
          .returns(expected[i].releaseYear(), CarResponse::releaseYear)
          .returns(expected[i].price(), CarResponse::price)
          .returns(expected[i].fuelConsumption(), CarResponse::fuelConsumption)
          .returns(expected[i].annualMaintenanceCost(), CarResponse::annualMaintenanceCost);

      assertThat(actual.createdAt()).isEqualToIgnoringNanos(expected[i].createdAt());
      assertThat(actual.updatedAt()).isEqualToIgnoringNanos(expected[i].updatedAt());

      final var x = i;
      Optional.ofNullable(actual.id()).ifPresent(id -> assertThat(id).isEqualTo(expected[x].id()));
    }
  }
}
