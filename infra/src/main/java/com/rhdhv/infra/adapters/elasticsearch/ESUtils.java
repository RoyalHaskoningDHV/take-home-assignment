package com.rhdhv.infra.adapters.elasticsearch;

import static com.rhdhv.infra.adapters.elasticsearch.ESConstants.ELASTICSEARCH_MAX_PAGINATION_LIMIT;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Slf4j
public final class ESUtils {

  public static int prepareOffset(final Pageable pageable) {
    final int total = (int) (pageable.getOffset() + pageable.getPageSize());

    if (total < ELASTICSEARCH_MAX_PAGINATION_LIMIT) {
      return (int) pageable.getOffset();
    }

    final int offset = ELASTICSEARCH_MAX_PAGINATION_LIMIT - pageable.getPageSize();

    log.warn("(Offset + pageSize) is {}, greater than the limit {}. Decreasing from to {}",
        total,
        ELASTICSEARCH_MAX_PAGINATION_LIMIT,
        offset
    );

    return offset;
  }


  /**
   * @param pageable    pagination detail
   * @param response    search response of ES action
   * @param modelMapper from document to model mapper
   * @param <M>         domain model type
   * @param <D>         ES document type
   * @return Page of model type
   */
  public static <M, D> Page<M> toPage(
      final Pageable pageable,
      final SearchResponse<? extends D> response,
      final Function<D, M> modelMapper) {

    final long totalDocumentCount = Optional.ofNullable(response.hits().total())
        .map(TotalHits::value)
        .orElse(0L);

    final List<M> models = response.hits().hits().stream()
        .map(Hit::source)
        .filter(Objects::nonNull)
        .map(modelMapper)
        .toList();

    return new PageImpl<>(models, pageable, totalDocumentCount);
  }

  private ESUtils() {
  }
}
