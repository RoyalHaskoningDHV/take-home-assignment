package com.rhdhv.infra.adapters.rest;

import static com.rhdhv.infra.adapters.rest.PaginationConstants.DEFAULT_PAGE_SIZE;
import static com.rhdhv.infra.adapters.rest.PaginationConstants.FIRST_PAGE;
import static com.rhdhv.infra.adapters.rest.PaginationConstants.PRICE;

import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.service.CarStoreFacade;
import com.rhdhv.domain.usecase.FullTextSearchCar;
import com.rhdhv.domain.usecase.RetrieveCarsByFilter;
import com.rhdhv.infra.adapters.rest.request.AddCarToStoreRequest;
import com.rhdhv.infra.adapters.rest.response.CarResponse;
import com.rhdhv.infra.adapters.rest.response.CarsResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(
    title = "Car Dealer",
    version = "1.0",
    description = "Car Dealer API"
)
)
@RequestMapping("/api/v1/car-dealer")
public class CarDealerController {

  private final CarStoreFacade carStoreFacade;

  @PostMapping
  @Operation(summary = "Add a car to store",
      responses = {
          @ApiResponse(responseCode = "201", description = "Car Response",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CarResponse.class))
          ),
          @ApiResponse(responseCode = "400", description = "Bad request",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))
          ),
          @ApiResponse(responseCode = "500", description = "Internal Server Error",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))
          )
      })
  public ResponseEntity<CarResponse> addCarToStore(@RequestBody @Valid final AddCarToStoreRequest request) {
    final Car car = this.carStoreFacade.addCar(request.toModel());

    return ResponseEntity.status(HttpStatus.CREATED).body(CarResponse.from(car));
  }

  @GetMapping
  @Operation(summary = "Retrieve cars by brand year",
      responses = {
          @ApiResponse(responseCode = "200", description = "Cars Response",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CarsResponse.class))
          ),
          @ApiResponse(responseCode = "400", description = "Bad request",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))
          ),
          @ApiResponse(responseCode = "500", description = "Internal Server Error",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))
          )
      })
  public ResponseEntity<CarsResponse> retrieve(
      @RequestParam(value = "year", required = false) final Integer year,
      @RequestParam(value = "brand", required = false) final String brand,
      @RequestParam(value = "page", required = false) final Integer page,
      @RequestParam(value = "size", required = false) @Max(DEFAULT_PAGE_SIZE) final Integer size,
      @RequestParam(value = "sort", required = false) final Sort.Direction direction,
      @RequestParam(value = "props", required = false) final String properties) {

    final PageRequest pageRequest = PageRequest.of(
        Objects.requireNonNullElse(page, FIRST_PAGE),
        Objects.requireNonNullElse(size, DEFAULT_PAGE_SIZE),
        Objects.requireNonNullElse(direction, Direction.ASC),
        Objects.requireNonNullElse(properties, PRICE)
    );

    final RetrieveCarsByFilter retrieveCarsByFilter = new RetrieveCarsByFilter(year, brand, pageRequest);

    final Page<Car> cars = this.carStoreFacade.retrieve(retrieveCarsByFilter);

    return ResponseEntity.ok(CarsResponse.from(cars));
  }


  @GetMapping("/search")
  @Operation(summary = "Search car by query",
      responses = {
          @ApiResponse(responseCode = "200", description = "Cars Response",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = CarsResponse.class))
          ),
          @ApiResponse(responseCode = "400", description = "Bad request",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))
          ),
          @ApiResponse(responseCode = "500", description = "Internal Server Error",
              content = @Content(mediaType = "application/problem+json",
                  schema = @Schema(implementation = ProblemDetail.class))
          )
      })
  public ResponseEntity<CarsResponse> search(
      @RequestParam(value = "query", required = false) final String query,
      @RequestParam(value = "page", required = false) final Integer page,
      @RequestParam(value = "size", required = false) final Integer size,
      @RequestParam(value = "sort", required = false) final Sort.Direction direction,
      @RequestParam(value = "props", required = false) final String properties) {

    final PageRequest pageRequest = PageRequest.of(
        Objects.requireNonNullElse(page, FIRST_PAGE),
        Objects.requireNonNullElse(size, DEFAULT_PAGE_SIZE),
        Objects.requireNonNullElse(direction, Direction.ASC),
        Objects.requireNonNullElse(properties, PRICE)
    );

    final FullTextSearchCar fullTextSearchCar = new FullTextSearchCar(query, pageRequest);

    final Page<Car> cars = this.carStoreFacade.search(fullTextSearchCar);

    return ResponseEntity.ok(CarsResponse.from(cars));
  }
}
