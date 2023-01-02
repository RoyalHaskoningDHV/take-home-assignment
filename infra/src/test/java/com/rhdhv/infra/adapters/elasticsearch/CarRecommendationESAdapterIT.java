package com.rhdhv.infra.adapters.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;

import com.rhdhv.domain.model.TotalAnnualCostOfCar;
import com.rhdhv.domain.usecase.RecommendCarsByLowestTotalAnnualCosts;
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
class CarRecommendationESAdapterIT {

  @Autowired
  private CarRecommendationESAdapter carRecommendationESAdapter;

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
  void findCarsByLowestTotalAnnualCost() {
    //given
    final RecommendCarsByLowestTotalAnnualCosts useCase = RecommendCarsByLowestTotalAnnualCosts.builder()
        .period(4)
        .travelDistancePerMonth(250)
        .fuelConsumption(BigDecimal.valueOf(0.55))
        .pageRequest(PageRequest.of(0, 100))
        .build();

    assertThat(useCase.totalTravelDistancePerYear()).isEqualTo(3000);
    assertThat(useCase.totalTravelDistance()).isEqualTo(12000);
    assertThat(useCase.totalFuelCost()).isEqualTo(new BigDecimal("6600.00"));
    assertThat(useCase.totalFuelCostPerYear()).isEqualTo(new BigDecimal("1650.00"));
    //when
    final Page<TotalAnnualCostOfCar> carsByLowestTotalAnnualCost = this.carRecommendationESAdapter.findCarsByLowestTotalAnnualCost(
        useCase);

    //then
    final List<TotalAnnualCostOfCar> content = carsByLowestTotalAnnualCost.getContent();
    assertThat(content).hasSize(3);

    assertThat(content.get(0).totalAnnualCost()).isEqualTo(new BigDecimal("940.0"));
    assertThat(content.get(1).totalAnnualCost()).isEqualTo(new BigDecimal("2050.0"));
    assertThat(content.get(2).totalAnnualCost()).isEqualTo(new BigDecimal("6980.0"));

  }
}