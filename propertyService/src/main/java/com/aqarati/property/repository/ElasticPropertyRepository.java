package com.aqarati.property.repository;

import com.aqarati.property.model.ElasticProperty;
import com.aqarati.property.model.Property;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


public interface ElasticPropertyRepository extends ElasticsearchRepository<ElasticProperty,Long> {
}
