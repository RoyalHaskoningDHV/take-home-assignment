package com.rhdhv.infra.adapters.web;

import static org.assertj.core.api.Assertions.assertThat;

import com.rhdhv.infra.AbstractIT;
import com.rhdhv.infra.adapters.web.request.AddCarToStoreRequest;
import com.rhdhv.infra.adapters.web.response.CarResponse;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarDealerControllerIT extends AbstractIT {

  @Autowired
  protected TestRestTemplate testRestTemplate;

  @LocalServerPort
  protected Integer port;

  @BeforeEach
  void setUp() {
    ELASTICSEARCH_CONTAINER.start();
  }

  @AfterEach
  void tearDown() {
    ELASTICSEARCH_CONTAINER.stop();
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
  }

  @Test
  void whenPostInvalidAddCarToStoreThen400() {
    //given
    final AddCarToStoreRequest request = AddCarToStoreRequest.builder()
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
        new RequestEntity<>(request, HttpMethod.POST, URI.create("/api/v1/car-dealer")),
        ProblemDetail.class
    );

    //then status
    assertThat(response).isNotNull()
        .returns(HttpStatus.BAD_REQUEST, ResponseEntity::getStatusCode);

    //then headers
    assertThat(response.getHeaders().getContentType())
        .isEqualTo(MediaType.APPLICATION_PROBLEM_JSON);

    // then body
    assertThat(response.getBody()).isNotNull();

    assertThat(response.getBody().getStatus()).isEqualTo(400);

    assertThat(response.getBody().getDetail())
        .contains("Wrong fields",
            "annualMaintenanceCost must not be null",
            "price must be greater than 0",
            "version must not be empty",
            "model must not be empty",
            "releaseYear must be greater than 0",
            "fuelConsumption must not be null",
            "brand must not be empty");

  }
}
