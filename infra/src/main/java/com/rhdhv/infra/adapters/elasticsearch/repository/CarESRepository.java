package com.rhdhv.infra.adapters.elasticsearch.repository;

import com.rhdhv.infra.adapters.elasticsearch.document.CarDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarESRepository extends ElasticsearchRepository<CarDocument, String> {

  Page<CarDocument> findByReleaseYear(Integer year, Pageable pageable);

  Page<CarDocument> findByBrand(String brand, Pageable pageRequest);

  Page<CarDocument> findByReleaseYearAndBrand(Integer year, String brand, Pageable pageRequest);
}
