package com.rhdhv.infra.adapters.rest;

import static com.rhdhv.infra.adapters.rest.PaginationConstants.DEFAULT_PAGE_SIZE;
import static com.rhdhv.infra.adapters.rest.PaginationConstants.FIRST_PAGE;

import com.rhdhv.domain.model.TotalAnnualCostOfCar;
import com.rhdhv.domain.service.CarStoreFacade;
import com.rhdhv.domain.usecase.RecommendCarsByLowestTotalAnnualCosts;
import com.rhdhv.infra.adapters.rest.response.RecommendationResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@OpenAPIDefinition(info = @Info(
    title = "Car Dealer Recommendation",
    version = "1.0",
    description = "Car Dealer Recommendation API"
)
)
@RequiredArgsConstructor
@RequestMapping("/api/v1/car-dealer/recommendation")
@Validated
public class CarRecommendationController {

  final CarStoreFacade carStoreFacade;

  @GetMapping
  @Operation(summary = "Recommend a car by total annual cost",
      responses = {
          @ApiResponse(responseCode = "200", description = "Recommendation Response",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = RecommendationResponse.class))
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
  public ResponseEntity<RecommendationResponse> recommendBy(
      @RequestParam("period") @Positive final int period,
      @RequestParam("dist_month") @Positive final int travelDistancePerMonth,
      @RequestParam("fuel_cons") @Positive final BigDecimal fuelConsumption,
      @RequestParam(value = "page", required = false) final Integer page,
      @RequestParam(value = "size", required = false) @Max(DEFAULT_PAGE_SIZE) final Integer size) {

    final PageRequest pageRequest = PageRequest.of(
        Objects.requireNonNullElse(page, FIRST_PAGE),
        Objects.requireNonNullElse(size, DEFAULT_PAGE_SIZE)
    );

    final RecommendCarsByLowestTotalAnnualCosts useCase = RecommendCarsByLowestTotalAnnualCosts.builder()
        .period(period)
        .fuelConsumption(fuelConsumption)
        .travelDistancePerMonth(travelDistancePerMonth)
        .pageRequest(pageRequest)
        .build();

    final Page<TotalAnnualCostOfCar> totalAnnualCostOfCars = this.carStoreFacade.recommend(useCase);

    return ResponseEntity.ok(
        RecommendationResponse.from(totalAnnualCostOfCars, useCase.totalFuelCost(), useCase.totalFuelCostPerYear(),
            useCase.totalTravelDistance(), useCase.totalTravelDistancePerYear())
    );
  }

}
