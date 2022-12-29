package com.rhdhv.infra.adapters.elasticsearch.repository;

import com.rhdhv.infra.adapters.elasticsearch.document.CarDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarESRepository extends ElasticsearchRepository<CarDocument, String> {

}
