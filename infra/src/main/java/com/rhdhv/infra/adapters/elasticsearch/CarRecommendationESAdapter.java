package com.rhdhv.infra.adapters.elasticsearch;

import static com.rhdhv.infra.adapters.elasticsearch.ESConstants.CARS_INDEX;
import static com.rhdhv.infra.adapters.elasticsearch.ESConstants.TOTAL_ANNUAL_COST;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.RuntimeField;
import co.elastic.clients.elasticsearch._types.mapping.RuntimeFieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest.Builder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import com.rhdhv.domain.model.Car;
import com.rhdhv.domain.model.TotalAnnualCostOfCar;
import com.rhdhv.domain.port.CarRecommendationPort;
import com.rhdhv.domain.usecase.RecommendCarsByLowestTotalAnnualCosts;
import com.rhdhv.infra.adapters.elasticsearch.document.CarDocument;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarRecommendationESAdapter implements CarRecommendationPort {

  private static final String TOTAL_ANNUAL_COST_FORMULA_SCRIPT = """
       double totalAnnualCost = doc['fuelConsumption'].value * %s + doc['annualMaintenanceCost'].value ;
       emit(totalAnnualCost);
      """;

  private final ElasticsearchClient elasticsearchClient;

  @SneakyThrows
  @Override
  public Page<TotalAnnualCostOfCar> findCarsByLowestTotalAnnualCost(final RecommendCarsByLowestTotalAnnualCosts useCase) {

    final RuntimeField totalAnnualCostCalculation = this.createRuntimeFieldForTotalAnnualCostCalculation(
        useCase.totalTravelDistance());

    final SearchRequest searchRequest = new Builder()
        .index(CARS_INDEX)
        .runtimeMappings(TOTAL_ANNUAL_COST, totalAnnualCostCalculation)
        //.query(rangeFilterByCost(useCase.givenAnnualCost()))
        .fields(fieldBuilder -> fieldBuilder.field(TOTAL_ANNUAL_COST))
        .sort(ascTotalAnnualCost())
        .from(ESUtils.prepareOffset(useCase.pageRequest()))
        .size(useCase.pageRequest().getPageSize())
        .build();

    final SearchResponse<CarDocument> response = this.elasticsearchClient.search(searchRequest, CarDocument.class);

    final List<TotalAnnualCostOfCar> cars = response.hits().hits().stream().mapMulti(this::toModel).toList();

    return new PageImpl<>(
        cars,
        PageRequest.of(useCase.pageRequest().getPageNumber(), cars.size()),
        Optional.ofNullable(response.hits().total()).map(TotalHits::value).orElse(0L)
    );
  }


  private RuntimeField createRuntimeFieldForTotalAnnualCostCalculation(final int totalTravelDistance) {
    final InlineScript totalAnnualCostFormula = new InlineScript.Builder()
        .source(TOTAL_ANNUAL_COST_FORMULA_SCRIPT.formatted(totalTravelDistance))
        .build();

    final Script totalAnnualCostFormulaScript = new Script.Builder()
        .inline(totalAnnualCostFormula)
        .build();

    return new RuntimeField.Builder()
        .type(RuntimeFieldType.Double)
        .script(totalAnnualCostFormulaScript)
        .build();
  }


  private static Function<SortOptions.Builder, ObjectBuilder<SortOptions>> ascTotalAnnualCost() {
    return builder -> builder.field(FieldSort.of(s -> s.field(TOTAL_ANNUAL_COST).order(SortOrder.Asc)));
  }

  private static Function<Query.Builder, ObjectBuilder<Query>> rangeFilterByCost(final BigDecimal givenAnnualCost) {
    return builder -> builder.bool(boolQuery ->
        boolQuery.filter(rangeQuery ->
            rangeQuery.range(rangeQueryBuilder ->
                rangeQueryBuilder.field(TOTAL_ANNUAL_COST).lte(JsonData.of(givenAnnualCost))
            )
        )
    );
  }

  private void toModel(final Hit<CarDocument> carDocumentHit, final Consumer<? super TotalAnnualCostOfCar> consumer) {
    if (Objects.nonNull(carDocumentHit.source())) {
      final Car car = carDocumentHit.source().toModel();

      final JsonData jsonData = carDocumentHit.fields().get(TOTAL_ANNUAL_COST);

      final BigDecimal totalAnnualCost = BigDecimal.valueOf((Double) jsonData.to(ArrayList.class).get(0));

      final TotalAnnualCostOfCar totalAnnualCostOfCar = new TotalAnnualCostOfCar(car, totalAnnualCost);

      consumer.accept(totalAnnualCostOfCar);
    }
  }

}
