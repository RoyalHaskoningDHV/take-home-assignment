package com.rhdhv.infra.adapters.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.model.TotalAnnualCostOfCar;
import com.rhdhv.domain.service.CarStoreFacade;
import com.rhdhv.infra.AbstractIT;
import com.rhdhv.infra.ElasticsearchContainerInitializer;
import com.rhdhv.infra.adapters.rest.request.AddCarToStoreRequest;
import com.rhdhv.infra.adapters.rest.response.RecommendationResponse;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

@AutoConfigureWebTestClient
@ContextConfiguration(initializers = ElasticsearchContainerInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarRecommendationControllerIT extends AbstractIT {

  @Autowired
  private CarStoreFacade carStoreFacade;

  private List<Car> cars;

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

    final var mercedes = AddCarToStoreRequest.builder()
        .brand("Mercedes")
        .model("A220")
        .version("A")
        .releaseYear(2022)
        .price(BigDecimal.valueOf(34000.0))
        .fuelConsumption(BigDecimal.valueOf(0.25))
        .annualMaintenanceCost(BigDecimal.valueOf(190.0))
        .build();

    this.cars = Stream.of(car, honda, mercedes).map(req -> this.carStoreFacade.addCar(req.toModel())).toList();
  }

  @AfterEach
  void tearDown() {
    this.carStoreFacade.deleteAll();
  }

  @Test
  void recommendByPeriodTravelDistancePerMonthAndFuelConsumptionThenReturnMatchedWith200() {
    //when
    final ResponseEntity<RecommendationResponse> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer/recommendation?period=4&dist_month=300&fuel_cons=0.60"), RecommendationResponse.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.OK, ResponseEntity::getStatusCode);

    // then body
    final RecommendationResponse carsResponse = response.getBody();
    assertThat(carsResponse)
        .returns(new BigDecimal("8640.00"), RecommendationResponse::totalFuelCost)
        .returns(new BigDecimal("2160.00"), RecommendationResponse::totalFuelCostPerYear)
        .returns(14400, RecommendationResponse::totalTravelDistance)
        .returns(3600, RecommendationResponse::totalTravelDistancePerYear);

    final TotalAnnualCostOfCar firstRecommendation = carsResponse.carsWithTotalAnnualCosts().get(0);
    assertThat(firstRecommendation)
        .returns(new BigDecimal("1090.0"), TotalAnnualCostOfCar::totalAnnualCost);

    final Car mercedes = this.cars.get(2);
    final Car firstCar = firstRecommendation.car();
    assertThat(firstCar)
        .returns(mercedes.brand(), Car::brand)
        .returns(mercedes.model(), Car::model)
        .returns(mercedes.version(), Car::version)
        .returns(mercedes.price(), Car::price)
        .returns(mercedes.annualMaintenanceCost(), Car::annualMaintenanceCost);

    final TotalAnnualCostOfCar secondRecommendation = carsResponse.carsWithTotalAnnualCosts().get(1);
    assertThat(secondRecommendation)
        .returns(new BigDecimal("2380.0"), TotalAnnualCostOfCar::totalAnnualCost);

    final Car honda = this.cars.get(1);
    final Car secondCar = secondRecommendation.car();
    assertThat(secondCar)
        .returns(honda.brand(), Car::brand)
        .returns(honda.model(), Car::model)
        .returns(honda.version(), Car::version)
        .returns(honda.price(), Car::price)
        .returns(honda.annualMaintenanceCost(), Car::annualMaintenanceCost);

    final TotalAnnualCostOfCar thirdRecommendation = carsResponse.carsWithTotalAnnualCosts().get(2);
    assertThat(thirdRecommendation)
        .returns(new BigDecimal("7376.0"), TotalAnnualCostOfCar::totalAnnualCost);

    final Car citroen = this.cars.get(0);

    final Car thirdCar = thirdRecommendation.car();

    assertThat(thirdCar)
        .returns(citroen.brand(), Car::brand)
        .returns(citroen.model(), Car::model)
        .returns(citroen.version(), Car::version)
        .returns(citroen.price(), Car::price)
        .returns(citroen.annualMaintenanceCost(), Car::annualMaintenanceCost);
  }


  @Test
  void recommendByMissingParamsThenReturn400() {

    //when
    final ResponseEntity<ProblemDetail> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer/recommendation"), ProblemDetail.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.BAD_REQUEST, ResponseEntity::getStatusCode);

    // then body
    final ProblemDetail problemDetail = response.getBody();
    assertThat(problemDetail).returns(400, ProblemDetail::getStatus)
        .returns("Required parameter 'period' is not present.", ProblemDetail::getDetail);
  }

  @Test
  void recommendByWrongParamsThenReturn400() {

    //when
    final ResponseEntity<ProblemDetail> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer/recommendation?period=-4&dist_month=-300&fuel_cons=0"), ProblemDetail.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.BAD_REQUEST, ResponseEntity::getStatusCode);

    // then body
    final ProblemDetail problemDetail = response.getBody();
    assertThat(problemDetail).returns(400, ProblemDetail::getStatus);
    assertThat(problemDetail.getDetail())
        .contains(
            "com.rhdhv.infra.adapters.rest.CarRecommendationController recommendBy.fuelConsumption: must be greater than 0",
            "com.rhdhv.infra.adapters.rest.CarRecommendationController recommendBy.period: must be greater than 0",
            "com.rhdhv.infra.adapters.rest.CarRecommendationController recommendBy.travelDistancePerMonth: must be greater than 0"
        );
  }

  @Test
  void recommendByMissingParams1ThenReturn400() {

    //when
    final ResponseEntity<ProblemDetail> response = this.testRestTemplate.getForEntity(
        URI.create("/api/v1/car-dealer/recommendation?period=3&fuel_cons=0.60"), ProblemDetail.class);

    //then status
    assertThat(response).isNotNull().returns(HttpStatus.BAD_REQUEST, ResponseEntity::getStatusCode);

    // then body
    final ProblemDetail problemDetail = response.getBody();
    assertThat(problemDetail)
        .returns(400, ProblemDetail::getStatus)
        .returns("Required parameter 'dist_month' is not present.", ProblemDetail::getDetail);
  }
}
